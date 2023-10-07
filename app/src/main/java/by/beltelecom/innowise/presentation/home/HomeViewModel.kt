package by.beltelecom.innowise.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.usecases.home.GetCollectionsUseCase
import by.beltelecom.innowise.domain.usecases.home.GetPopularUseCase
import by.beltelecom.innowise.domain.usecases.home.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularUseCase: GetPopularUseCase,
    private val searchUseCase: SearchUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase
) : ViewModel() {

    private val _state by lazy {
        MutableLiveData<HomeUIState>(HomeUIState.Loading(source = Source.Featured))
    }
    val state: LiveData<HomeUIState>
        get() = _state

    private val compositeDisposable = CompositeDisposable()

    private val requestPublisher = PublishSubject.create<Unit>()
    private val pagingRequestsObservable: Observable<Unit> = requestPublisher.debounce(1, TimeUnit.SECONDS)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    init {
        getCollections()
        observePagingRequests()
    }

    fun getPopular(page: Int = 1) {
        getPopularUseCase(page)
            .doOnSubscribe {
                _state.setValue(
                    HomeUIState.Loading(
                        source = Source.Featured,
                        collections = state.value?.collections
                    )
                )
            }
            .subscribe(
                {
                    _state.setValue(
                        HomeUIState.Success(
                            source = Source.Featured,
                            collections = state.value?.collections,
                            page = it.page,
                            total = it.total,
                            photos = it.photos
                        )
                    )
                },
                {
                    _state.value = HomeUIState.Error(Source.Featured)
                }
            ).addTo(compositeDisposable)
    }

    private fun getCollections() {
        getCollectionsUseCase()
            .doOnSubscribe { _state.setValue(HomeUIState.Loading(Source.Collections)) }
            .subscribe(
                {
                    _state.value = HomeUIState.Success(
                        source = Source.Collections,
                        collections = it,
                        page = 1,
                        photos = null,
                        total = 0
                    )
                    getPopular()
                },
                {
                    _state.setValue(HomeUIState.Error(Source.Collections))
                }
            ).addTo(compositeDisposable)
    }

    fun search(query: String, page: Int = 1) {
        searchUseCase(query, page)
            .doOnSubscribe {
                _state.setValue(
                    HomeUIState.Loading(
                        source = Source.Search(query),
                        collections = state.value?.collections
                    )
                )
            }
            .subscribe(
                {
                    _state.setValue(
                        HomeUIState.Success(
                            source = Source.Search(query),
                            collections = state.value?.collections,
                            photos = it.photos,
                            page = it.page,
                            total = it.total
                        )
                    )
                },
                {
                    _state.setValue(
                        HomeUIState.Error(
                            source = Source.Search(query),
                            collections = state.value?.collections
                        )
                    )
                }
            ).addTo(compositeDisposable)
    }

    fun loadMore() {
        requestPublisher.onNext(Unit)
    }

    private fun observePagingRequests() {
        pagingRequestsObservable.subscribe(
            {
                when (val localState = state.value) {
                    is HomeUIState.Success -> {
                        when (localState.source) {
                            is Source.Featured -> {
                                getPopularUseCase(page = localState.page + 1)
                                    .subscribe(
                                        {
                                            _state.setValue(
                                                HomeUIState.Success(
                                                    source = Source.Featured,
                                                    collections = state.value?.collections,
                                                    page = it.page,
                                                    total = it.total,
                                                    photos = localState.photos?.plus(it.photos)
                                                )
                                            )
                                        },
                                        {
                                            _state.value = HomeUIState.Error(Source.Featured)
                                        }
                                    ).addTo(compositeDisposable)
                            }

                            is Source.Search -> {
                                searchUseCase(localState.source.query, localState.page + 1)
                                    .subscribe(
                                        {
                                            _state.setValue(
                                                HomeUIState.Success(
                                                    source = Source.Search(query = localState.source.query),
                                                    collections = state.value?.collections,
                                                    page = it.page,
                                                    total = it.total,
                                                    photos = localState.photos?.plus(it.photos)
                                                )
                                            )
                                        },
                                        {
                                            _state.value = HomeUIState.Error(Source.Search(query = localState.source.query))
                                        }
                                    ).addTo(compositeDisposable)
                            }

                            else -> {}
                        }
                    }

                    else -> {}
                }
            },
            { throwable ->
                _state.value = state.value?.source?.let { source -> HomeUIState.Error(source = source) }
            }
        ).addTo(compositeDisposable)
    }
}
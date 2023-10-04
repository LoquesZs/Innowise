package by.beltelecom.innowise.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.usecases.home.GetCollectionsUseCase
import by.beltelecom.innowise.domain.usecases.home.GetPopularUseCase
import by.beltelecom.innowise.domain.usecases.home.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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

    private val publish = PublishSubject.create<Boolean>()
    private val debounceObservable: Observable<Boolean> = publish.debounce(1, TimeUnit.SECONDS)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    init {
        getCollections()
        pagingObserver()
    }

    fun getPopular(page: Int = 1) {
        val result = getPopularUseCase(page)
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
            )
        compositeDisposable.add(result)
    }

    private fun getCollections() {
        val result = getCollectionsUseCase()
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
            )
        compositeDisposable.add(result)
    }

    fun search(query: String, page: Int = 1) {
        val result = searchUseCase(query, page)
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
            )
        compositeDisposable.add(result)
    }

    fun loadMore() {
        publish.onNext(true)
    }

    private fun pagingObserver() {
        val result = debounceObservable.subscribe(
            {
                when (val localState = state.value) {
                    is HomeUIState.Success -> {
                        when (localState.source) {
                            is Source.Featured -> {
                                val result = getPopularUseCase(page = localState.page + 1).subscribe(
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
                                )
                                compositeDisposable.add(result)
                            }

                            is Source.Search -> {
                                val result = searchUseCase(localState.source.query, localState.page + 1).subscribe(
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
                                )
                                compositeDisposable.add(result)
                            }
                            else -> {}
                        }
                    }

                    else -> {}
                }
            },
            {
                _state.value = state.value?.source?.let { it1 -> HomeUIState.Error(source = it1) }
            }
        )
        compositeDisposable.add(result)
    }
}
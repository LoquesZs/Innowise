package by.beltelecom.innowise.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.repository.HomeRepository
import by.beltelecom.innowise.domain.usecases.home.GetCollectionsUseCase
import by.beltelecom.innowise.domain.usecases.home.GetPopularUseCase
import by.beltelecom.innowise.domain.usecases.home.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    init {
        getCollections()
    }

    fun getPopular(page: Int = 1) {
        val result = getPopularUseCase(page)
            .doOnSubscribe {
                _state.setValue(HomeUIState.Loading(source = Source.Featured, collections = state.value?.collections))
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
            .doOnSubscribe { _state.setValue(HomeUIState.Loading(source = Source.Search(query), collections = state.value?.collections)) }
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
        when(val localState = state.value) {
            is HomeUIState.Success -> {
                when (localState.source) {
                    is Source.Featured -> getPopularUseCase(page = localState.page + 1)
                    is Source.Search -> search(localState.source.query, localState.page + 1)
                    else -> {}
                }
            }
            else -> {}
        }
    }
}
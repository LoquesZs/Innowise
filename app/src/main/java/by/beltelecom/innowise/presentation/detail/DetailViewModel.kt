package by.beltelecom.innowise.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.usecases.detail.AddToBookmarksUseCase
import by.beltelecom.innowise.domain.usecases.detail.GetPhotoUseCase
import by.beltelecom.innowise.domain.usecases.detail.RemoveFromBookmarksUseCase
import by.beltelecom.innowise.domain.usecases.detail.SubscribeToBookmarkStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val subscribeToBookmarkStateUseCase: SubscribeToBookmarkStateUseCase,
    private val addToBookmarksUseCase: AddToBookmarksUseCase,
    private val removeFromBookmarksUseCase: RemoveFromBookmarksUseCase
) : ViewModel() {

    private val _photo: MutableLiveData<Photo> by lazy { MutableLiveData() }
    val photo: LiveData<Photo>
        get() = _photo

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData(true) }
    val loading: LiveData<Boolean>
        get() = _loading

    private val _bookmark: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val bookmark: LiveData<Boolean>
        get() = _bookmark

    private val _error: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val error: LiveData<Boolean>
        get() = _error

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setID(id: Long) {
        _loading.postValue(true)
        getBookmarkState(id)
        getPhotoUseCase(id)
            .subscribe(
                {
                    _loading.postValue(false)
                    _photo.postValue(it)
                },
                {
                    _error.postValue(true)
                }
            ).addTo(compositeDisposable)
    }

    fun addToBookmarks() {
        _photo.value?.let { photo ->
            addToBookmarksUseCase(photo)
                .subscribe(
                    {},
                    {
                        it.printStackTrace()
                    }
                ).addTo(compositeDisposable)
        }
    }

    fun removeFromBookmarks() {
        _photo.value?.let { photo ->
            removeFromBookmarksUseCase(photo)
                .subscribe(
                    {},
                    {
                        it.printStackTrace()
                    }
                ).addTo(compositeDisposable)
        }
    }

    private fun getBookmarkState(id: Long) {
        subscribeToBookmarkStateUseCase(id)
            .subscribe(
                {
                    _bookmark.postValue(it)
                },
                {
                    it.printStackTrace()
                }
            ).addTo(compositeDisposable)
    }
}
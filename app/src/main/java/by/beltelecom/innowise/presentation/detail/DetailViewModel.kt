package by.beltelecom.innowise.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.usecases.detail.AddToBookmarksUseCase
import by.beltelecom.innowise.domain.usecases.detail.GetBookmarkStateUseCase
import by.beltelecom.innowise.domain.usecases.detail.GetPhotoUseCase
import by.beltelecom.innowise.domain.usecases.detail.RemoveFromBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val getBookmarkStateUseCase: GetBookmarkStateUseCase,
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
        val photoResult = getPhotoUseCase(id)
            .subscribe(
                {
                    _loading.postValue(false)
                    _photo.postValue(it)
                },
                {
                    _error.postValue(true)
                }
            )
        compositeDisposable.add(photoResult)
    }

    fun addToBookmarks() {
        _photo.value?.let { photo ->
            val result = addToBookmarksUseCase(photo)
                .subscribe(
                    {
                        _bookmark.postValue(true)
                    },
                    {
                        it.printStackTrace()
                    }
                )

            compositeDisposable.add(result)
        }
    }

    fun removeFromBookmarks() {
        _photo.value?.let { photo ->
            val result = removeFromBookmarksUseCase(photo)
                .subscribe(
                    {
                        if (it > 0) _bookmark.postValue(false)
                    },
                    {
                        it.printStackTrace()
                    }
                )
            compositeDisposable.add(result)
        }
    }

    private fun getBookmarkState(id: Long) {
        val result = getBookmarkStateUseCase(id)
            .subscribe(
                {
                    _bookmark.postValue(it)
                },
                {
                    it.printStackTrace()
                }
            )

        compositeDisposable.add(result)
    }
}
package by.beltelecom.innowise.presentation.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.usecases.bookmarks.GetBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase
) : ViewModel() {

    private val _bookmarks by lazy { MutableLiveData<PagingData<Photo>>() }
    val bookmarks: LiveData<PagingData<Photo>>
        get() = _bookmarks

    private val _loading by lazy { MutableLiveData(true) }
    val loading: LiveData<Boolean>
        get() = _loading

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    init {
        getBookmarks()
    }

    private fun getBookmarks() {
        getBookmarksUseCase()
            .subscribe( { photos ->
                Log.d("Viewmodel", "photos: $photos")
                _loading.postValue(false)
                _bookmarks.postValue(photos)
            }, {}).addTo(compositeDisposable)
    }
}
package by.beltelecom.innowise.presentation.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.usecases.bookmarks.GetBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getBookmarksUseCase: GetBookmarksUseCase,
) : ViewModel() {

    private val _bookmarks by lazy { MutableLiveData<List<Photo>>() }
    val bookmarks: LiveData<List<Photo>>
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
        val result = getBookmarksUseCase()
            .subscribe { photos ->
                _loading.postValue(false)
                _bookmarks.postValue(photos)
            }
        compositeDisposable.add(result)
    }
}
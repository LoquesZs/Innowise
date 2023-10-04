package by.beltelecom.innowise.domain.usecases.detail

import by.beltelecom.innowise.common.database.dao.DetailDao
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetBookmarkStateUseCase @Inject constructor(
    private val dao: DetailDao
) {

    operator fun invoke(id: Long): Observable<Boolean> {
        return dao.getBookmarkState(id)
    }
}
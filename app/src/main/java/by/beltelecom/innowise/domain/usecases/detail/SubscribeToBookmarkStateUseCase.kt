package by.beltelecom.innowise.domain.usecases.detail

import by.beltelecom.innowise.common.database.dao.DetailDao
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class SubscribeToBookmarkStateUseCase @Inject constructor(
    private val dao: DetailDao
) {

    operator fun invoke(id: Long): Flowable<Boolean> {
        return dao.getBookmarkState(id)
    }
}
package by.beltelecom.innowise.common.workmanager

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import by.beltelecom.innowise.common.network.RetrofitClientFactory
import com.bumptech.glide.Glide
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import okhttp3.Cache

class CacheClearWorker(appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams) {
    override fun createWork(): Single<Result> {
        return Single.fromCallable {
            Glide.get(applicationContext).apply {
                clearDiskCache()
            }
            val okhttpCache = Cache(
                directory = File(applicationContext.cacheDir, RetrofitClientFactory.CACHE_NAME),
                maxSize = RetrofitClientFactory.CACHE_MAX_SIZE
            )
            okhttpCache.apply {
                delete()
                evictAll()
            }
            Result.success()
        }.subscribeOn(Schedulers.io())
    }
}
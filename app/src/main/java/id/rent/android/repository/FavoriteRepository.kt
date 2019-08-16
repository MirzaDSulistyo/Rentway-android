package id.rent.android.repository

import androidx.lifecycle.LiveData
import id.rent.android.data.api.ApiService
import id.rent.android.data.database.AppDb
import id.rent.android.data.database.dao.FavoriteDao
import id.rent.android.data.vo.Resource
import id.rent.android.model.Favorite
import id.rent.android.model.FavoriteList
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val dao: FavoriteDao,
    private val apiService: ApiService
) {
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun get(token: String): LiveData<Resource<List<Favorite>>> {
        return object : NetworkBoundResource<List<Favorite>, FavoriteList>(appExecutors) {
            override fun saveCallResult(item: FavoriteList) {
                db.beginTransaction()
                try {
                    dao.insert(item.data!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

//            override fun shouldFetch(data: List<Product>?): Boolean {
//                return true
//            }

            override fun shouldFetch(data: List<Favorite>?) = true

            override fun createCall() = apiService.getFavorite(token)

            override fun loadFromDb() = dao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}
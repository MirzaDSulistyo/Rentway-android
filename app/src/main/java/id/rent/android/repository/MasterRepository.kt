package id.rent.android.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import id.rent.android.data.api.ApiService
import id.rent.android.data.database.AppDb
import id.rent.android.data.database.dao.MasterDao
import id.rent.android.data.vo.Resource
import id.rent.android.model.Master
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.RateLimiter
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val masterDao: MasterDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getDataMaster(token: String): LiveData<Resource<Master>> {
        return object : NetworkBoundResource<Master, Master>(appExecutors) {
            override fun saveCallResult(item: Master) {
                Timber.d("saveCallResult ${Gson().toJson(item.rentway)}")

                db.beginTransaction()
                try {
                    Timber.d("saveCallResult ${Gson().toJson(item.rentway)}")
                    val i = masterDao.inserts(item)
                    Timber.d("saveCallResult $i")
                    //categoryDao.inserts(item.categories!!)
                    //paymentTypeDao.insertPayments(item.payments!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: Master?) = data == null

//            override fun shouldFetch(data: Master?): Boolean {
//                return data?.rentway == null || data.payments == null || data.categories == null
//            }

            /*override fun shouldFetch(data: Master?): Boolean {
                //return data == null || data.isEmpty() || rateLimit.shouldFetch(token)
                return data == null ||rateLimit.shouldFetch(token)
            }*/

            override fun createCall() = apiService.getDataMaster(token)

//            override fun createCall(): LiveData<ApiResponse<Master>> {
//                val response = apiService.getDataMaster(token)
//            }

            override fun loadFromDb(): LiveData<Master> = masterDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}
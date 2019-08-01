package id.rent.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rent.android.data.api.*
import id.rent.android.data.dao.ProductDao
import id.rent.android.data.database.AppDb
import id.rent.android.data.vo.Resource
import id.rent.android.model.Product
import id.rent.android.model.ProductList
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.RateLimiter
import okhttp3.RequestBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val productDao: ProductDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getProducts(token: String): LiveData<Resource<List<Product>>> {
        return object : NetworkBoundResource<List<Product>, ProductList>(appExecutors) {
            override fun saveCallResult(item: ProductList) {
                db.beginTransaction()
                try {
                    productDao.insertProducts(item.products!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getProduct(token)

            override fun loadFromDb() = productDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }

    fun save(token: String, body: RequestBody): LiveData<Resource<Product>> {

        val data = MutableLiveData<Resource<Product>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.postProduct(token, body).execute()

                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            } catch (e: ConnectException) {
                data.postValue(Resource.error("Connection Error", null))
            }

        }

        return data
    }

    fun update(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Product>> {

        val data = MutableLiveData<Resource<Product>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.putProduct(token, id, fields).execute()

                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            }

        }

        return data
    }

    fun delete(token: String, id: String): LiveData<Resource<Product>> {

        val data = MutableLiveData<Resource<Product>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.deleteProduct(token, id).execute()

                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))

                        db.beginTransaction()
                        try {
                            productDao.deleteByProductId(id)
                            db.setTransactionSuccessful()
                        }finally {
                            db.endTransaction()
                        }
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            }
        }

        return data
    }
}
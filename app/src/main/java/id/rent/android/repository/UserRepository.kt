package id.rent.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rent.android.data.api.*
import id.rent.android.data.dao.UserDao
import id.rent.android.data.database.AppDb
import id.rent.android.data.vo.Resource
import id.rent.android.model.Auth
import id.rent.android.model.Profile
import id.rent.android.utility.AppExecutors
import id.rent.android.utility.RateLimiter
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val userDao: UserDao,
    private val apiService: ApiService
) {
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {

        val data = MutableLiveData<Resource<Auth>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.login(email, pass).execute()

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

    fun profile(token: String): LiveData<Resource<Profile>> {

        val data = MutableLiveData<Resource<Profile>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.profile(token).execute()

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
}
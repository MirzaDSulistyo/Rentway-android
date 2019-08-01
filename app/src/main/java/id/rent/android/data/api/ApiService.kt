package id.rent.android.data.api

import id.rent.android.model.Auth
import id.rent.android.model.Profile
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    /* ====== AUTHENTICATION ===== */

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Auth>

    /* ====== PROFILE ===== */

    @GET("users/profile")
    fun profile(
        @Header("token") token: String
    ): Call<Profile>
}
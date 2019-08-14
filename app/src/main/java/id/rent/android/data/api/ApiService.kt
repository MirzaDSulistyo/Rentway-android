package id.rent.android.data.api

import androidx.lifecycle.LiveData
import id.rent.android.model.*
import okhttp3.RequestBody
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

    /* ===== PRODUCT ===== */

    @GET("product")
    fun getProduct(
        @Header("token") token: String
    ): LiveData<ApiResponse<ProductList>>

    @GET("product/list/{id}")
    fun getProductByStore(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<ProductList>

    @POST("product/new")
    fun postProduct(
        @Header("token") token: String,
        @Body body: RequestBody
    ): Call<Product>

    @FormUrlEncoded
    @PUT("product/{id}")
    fun putProduct(
        @Header("token") token: String,
        @Path("id") id: String,
        @FieldMap fields: Map<String, String>
    ): Call<Product>

    @DELETE("product/{id}")
    fun deleteProduct(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Product>

    /* ===== MASTER ===== */

    @GET("master")
    fun getDataMaster(
        @Header("token") token: String
    ): LiveData<ApiResponse<Master>>
}
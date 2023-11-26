package com.example.myapplication.retrofit

import com.example.myapplication.entity.AuthRequest
import com.example.myapplication.entity.Product
import com.example.myapplication.entity.Products
import com.example.myapplication.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MainApi {

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id:Int): Product

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @Headers("Content-Type: application/json")
    @GET("products")
    suspend fun getAllItems(@Header("Authorization") token:String): Products

    @Headers("Content-Type: application/json")
    @GET("products/search")
    suspend fun searchByProductAuth(@Header("Authorization") token:String,
                                    @Query("q") name: String): Products
}
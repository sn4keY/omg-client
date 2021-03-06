package com.norbertneudert.openmygarage.service

import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.norbertneudert.openmygarage.service.auth.AuthInterceptor
import com.norbertneudert.openmygarage.service.auth.TokenAuthenticator
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://openmygarage.ddns.net/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .authenticator(TokenAuthenticator())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

interface OMGApiService {
    @GET("main/gate")
    fun openGate() : Call<Void>

    @GET("entrylog/getall")
    suspend fun getEntryLogs() : List<EntryLog>

    @GET("entrylog/picture/{logId}")
    suspend fun getEntryLogPicture(@Path("logId") logId: Long) : ResponseBody

    @GET("storedplate/getall")
    suspend fun getStoredPlates() : List<StoredPlate>

    @POST("storedplate/add")
    fun addStoredPlate(@Body storedPlate: StoredPlate) : Call<Void>

    @HTTP(method = "DELETE", path = "storedplate/delete/{id}", hasBody = true)
    fun deleteStoredPlate(@Path("id") id: Long) : Call<Void>

    @POST("storedplate/update")
    fun updateStoredPlate(@Body storedPlate: StoredPlate) : Call<Void>

    @POST("authentication/login")
    suspend fun login(@Body loginViewModel: LoginViewModel) : TokenViewModel
}

object OMGApi {
    val retrofitService: OMGApiService by lazy { retrofit.create(OMGApiService::class.java) }
}
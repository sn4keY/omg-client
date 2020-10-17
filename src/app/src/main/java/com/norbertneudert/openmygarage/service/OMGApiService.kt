package com.norbertneudert.openmygarage.service

import android.graphics.Bitmap
import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://192.168.1.180/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OMGApiService {
    @GET("main/gate")
    fun openGate(@Header("Authorization") token: String) : Call<Void>

    @GET("entrylog/getall")
    suspend fun getEntryLogs(@Header("Authorization") token: String) : List<EntryLog>

    @GET("entrylog/picture/{logId}")
    suspend fun getEntryLogPicture(@Path("logId") logId: Long, @Header("Authorization") token: String) : Bitmap // TODO: test

    @GET("storedplate/getall")
    suspend fun getStoredPlates(@Header("Authorization") token: String) : List<StoredPlate>

    @POST("storedplate/add")
    fun addStoredPlate(@Body storedPlate: StoredPlate, @Header("Authorization") token: String) : Call<Void>

    @DELETE("storedplate/delete")
    fun deleteStoredPlate(@Body storedPlate: StoredPlate, @Header("Authorization") token: String) : Call<Void>

    @POST("storedplate/update")
    fun updateStoredPlate(@Body storedPlate: StoredPlate, @Header("Authorization") token: String) : Call<Void>

    @POST("authentication/login")
    suspend fun login(@Body loginViewModel: LoginViewModel) : TokenViewModel
}

object OMGApi {
    val retrofitService: OMGApiService by lazy { retrofit.create(OMGApiService::class.java) }
}
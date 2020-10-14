package com.norbertneudert.openmygarage.service

import android.graphics.Bitmap
import com.norbertneudert.openmygarage.data.entities.EntryLog
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://192.168.1.180:44383/api"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    //.addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface OMGApiService {
    @GET("/main/gate")
    fun openGate() : Call<Void>

    @GET("/entrylog/getall")
    fun getEntryLogs() : Deferred<List<EntryLog>>

    @GET("/entrylog/picture/{logId}")
    fun getEntryLogPicture(@Path("logId") logId: Long) : Call<Bitmap>

    @GET("/storedplate/getall")
    fun getStoredPlates() : Deferred<List<StoredPlate>>

    @POST("/storedplate/add")
    fun addStoredPlate(@Body storedPlate: StoredPlate) : Call<Void>

    @DELETE("/storedplate/delete")
    fun deleteStoredPlate(@Body storedPlate: StoredPlate) : Call<Void>

    @POST("/storedplate/update")
    fun updateStoredPlate(@Body storedPlate: StoredPlate) : Call<Void>

    @POST("/authentication/login")
    fun login() : Call<String>
}
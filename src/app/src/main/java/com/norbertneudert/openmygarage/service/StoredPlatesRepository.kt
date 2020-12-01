package com.norbertneudert.openmygarage.service

import android.util.Log
import com.norbertneudert.openmygarage.data.dao.StoredPlateDao
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoredPlatesRepository(private val storedPlatesDao: StoredPlateDao) {
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    init {
        clearDatabase()
        refreshDatabase(null)
    }

    companion object  {
        @Volatile
        private var INSTANCE: StoredPlatesRepository? = null

        fun getInstance(storedPlatesDao: StoredPlateDao): StoredPlatesRepository {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = StoredPlatesRepository(storedPlatesDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun addStoredPlate(storedPlate: StoredPlate) {
        coroutineScope.launch {
            onAddStoredPlate(storedPlate)
        }
    }

    suspend fun get(plate: String): StoredPlate? {
        return withContext(Dispatchers.IO) {
            storedPlatesDao.get(plate)
        }
    }

    private suspend fun onAddStoredPlate(storedPlate: StoredPlate) {
        val lastPlate = getLastStoredPlate()
        if (lastPlate != null) {
            storedPlate.id = lastPlate.id + 1
        }
        OMGApi.retrofitService.addStoredPlate(storedPlate).enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // TODO("log")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refreshDatabase(storedPlate)
            }
        })
    }

    fun deleteStoredPlate(id: Long) {
        OMGApi.retrofitService.deleteStoredPlate(id).enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("ApiHandlerStoredPlates", t.message!!)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearDatabase()
                refreshDatabase(null)
            }
        })
    }

    fun updateStoredPlate(storedPlate: StoredPlate) {
        OMGApi.retrofitService.updateStoredPlate(storedPlate).enqueue(object: Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // TODO("log")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refreshDatabase(storedPlate)
            }
        })
    }

    fun refreshDatabase(update: StoredPlate?) : Boolean {
        coroutineScope.launch {
            val getStoredPlates = OMGApi.retrofitService.getStoredPlates()
            if (getStoredPlates.isNotEmpty()) {
                populateStoredPlates(getStoredPlates, update)
            }
        }
        return false
    }

    private suspend fun populateStoredPlates(listResult: List<StoredPlate>, update: StoredPlate?) {
        if (!isPlatesUpdated(listResult)) {
            val plateList = getStoredPlatesFromInApp()
            var index = if (plateList?.count() != 0) { listResult.count() - (listResult.count() - plateList!!.count()) } else { 0 }
            while (index < listResult.count()) {
                withContext(Dispatchers.IO) {
                    storedPlatesDao.insert(listResult[index])
                }
                index++
            }
        }
        if (update != null) {
            withContext(Dispatchers.IO) {
                storedPlatesDao.insert(update)
            }
        }
    }

    private suspend fun getStoredPlatesFromInApp(): List<StoredPlate>? {
        return withContext(Dispatchers.IO) {
            storedPlatesDao.getStoredPlatesList()
        }
    }

    private suspend fun isPlatesUpdated(listResult: List<StoredPlate>): Boolean {
        val lastPlate = getLastStoredPlate()
        if (lastPlate?.id == listResult.last().id) {
            return true
        }
        return false
    }

    private suspend fun getLastStoredPlate(): StoredPlate? {
        return withContext(Dispatchers.IO) {
            storedPlatesDao.getLastStoredPlate()
        }
    }

    private fun clearDatabase() {
        coroutineScope.launch {
            onClearDatabase()
        }
    }

    private suspend fun onClearDatabase() {
        return withContext(Dispatchers.IO) {
            storedPlatesDao.clear()
        }
    }
}
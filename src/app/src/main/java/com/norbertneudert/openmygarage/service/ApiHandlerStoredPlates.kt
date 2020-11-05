package com.norbertneudert.openmygarage.service

import com.norbertneudert.openmygarage.data.dao.StoredPlateDao
import com.norbertneudert.openmygarage.data.entities.StoredPlate
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiHandlerStoredPlates(private val storedPlatesDao: StoredPlateDao) {
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    init {
        clearDatabase()
        refreshDatabase()
    }

    fun addStoredPlate(storedPlate: StoredPlate) {
        OMGApi.retrofitService.addStoredPlate(storedPlate).enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // TODO("log")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refreshDatabase()
            }
        })
    }

    fun deleteStoredPlate(id: Long) {
        OMGApi.retrofitService.deleteStoredPlate(id).enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // TODO("log")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refreshDatabase()
            }
        })
    }

    fun updateStoredPlate(storedPlate: StoredPlate) {
        OMGApi.retrofitService.updateStoredPlate(storedPlate).enqueue(object: Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // TODO("log")
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refreshDatabase()
            }
        })
    }

    fun refreshDatabase() : Boolean {
        coroutineScope.launch {
            val getStoredPlates = OMGApi.retrofitService.getStoredPlates()
            populateStoredPlates(getStoredPlates)
        }
        return false
    }

    private suspend fun populateStoredPlates(listResult: List<StoredPlate>) {
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
    }

    private suspend fun getStoredPlatesFromInApp(): List<StoredPlate>? {
        return withContext(Dispatchers.IO) {
            storedPlatesDao.getStoredPlatesList()
        }
    }

    private suspend fun isPlatesUpdated(listResult: List<StoredPlate>): Boolean {
        val lastPlate = getLastStoredPlate()
        if (lastPlate?.plateId == listResult.last().plateId) {
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
package com.norbertneudert.openmygarage.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.norbertneudert.openmygarage.data.dao.EntryLogDao
import com.norbertneudert.openmygarage.data.entities.EntryLog
import kotlinx.coroutines.*

class EntryLogsRepository private constructor(private val entryLogsDao: EntryLogDao){
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    init {
        clearDatabase()
        refreshDatabase()
    }

    companion object {
        @Volatile
        private var INSTANCE: EntryLogsRepository? = null

        fun getInstance(entryLogsDao: EntryLogDao) : EntryLogsRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = EntryLogsRepository(entryLogsDao)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    suspend fun getPicture(id: Long) : Bitmap {
        val image = GlobalScope.async {
            OMGApi.retrofitService.getEntryLogPicture(id)
        }.await()
        return BitmapFactory.decodeStream(image.byteStream())
    }

    fun refreshDatabase() : Boolean {
        coroutineScope.launch {
            val getEntryLogs = OMGApi.retrofitService.getEntryLogs()
            populateEntryLogs(getEntryLogs)
        }
        return false
    }

    private suspend fun populateEntryLogs(entryLogs: List<EntryLog>) {
        if (!isLogsUpdated(entryLogs)) {
            val logList = getEntryLogsFromInApp()
            var index = if(logList?.count() != 0) { entryLogs.count() - (entryLogs.count() - logList!!.count())} else { 0 }
            while (index < entryLogs.count()) {
                withContext(Dispatchers.IO) {
                    entryLogsDao.insert(entryLogs[index])
                }
                index++
            }
        }
    }

    private suspend fun getEntryLogsFromInApp() : List<EntryLog>? {
        return withContext(Dispatchers.IO) {
            entryLogsDao.getEntryLogsList()
        }
    }

    private suspend fun isLogsUpdated(entryLogs: List<EntryLog>) : Boolean {
        val lastLog = getLastEntryLog()
        if (lastLog?.entryTime == entryLogs.last().entryTime) {
            return true
        }
        return false
    }

    private suspend fun getLastEntryLog() : EntryLog? {
        return withContext(Dispatchers.IO) {
            entryLogsDao.getLastEntryLog()
        }
    }

    private fun clearDatabase() {
        coroutineScope.launch {
            onClearDatabase()
        }
    }

    private suspend fun onClearDatabase() {
        withContext(Dispatchers.IO) {
            entryLogsDao.clear()
        }
    }
}
package com.norbertneudert.openmygarage.ui.logs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.norbertneudert.openmygarage.data.dao.EntryLogDao
import java.lang.IllegalArgumentException

class LogsViewModelFactory (private val dao: EntryLogDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogsViewModel::class.java)) {
            return LogsViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
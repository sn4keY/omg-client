package com.norbertneudert.openmygarage.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.norbertneudert.openmygarage.data.dao.EntryLogDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeFragmentViewModel(val dao: EntryLogDao, application: Application) : AndroidViewModel(application) {
    val logs = dao.getEntryLogsLimited()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
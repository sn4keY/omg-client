package com.norbertneudert.openmygarage.ui.logs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.norbertneudert.openmygarage.data.dao.EntryLogDao

class LogsViewModel(val dao: EntryLogDao, application: Application) : AndroidViewModel(application) {
    val logs = dao.getEntryLogs()
}
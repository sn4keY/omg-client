package com.norbertneudert.openmygarage.ui.plates

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.norbertneudert.openmygarage.data.dao.StoredPlateDao
import java.lang.IllegalArgumentException

class PlatesViewModelFactory(private val dao: StoredPlateDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlatesViewModel::class.java)) {
            return PlatesViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
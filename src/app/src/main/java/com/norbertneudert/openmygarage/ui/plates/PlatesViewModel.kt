package com.norbertneudert.openmygarage.ui.plates

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.norbertneudert.openmygarage.data.dao.StoredPlateDao

class PlatesViewModel(val dao: StoredPlateDao, application: Application) : AndroidViewModel(application) {
    val plates = dao.getStoredPlates()
}
package com.norbertneudert.openmygarage.service.auth

import android.app.Activity
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.*

class ApiHandlerAuthentication(private val activity: Activity) {
    suspend fun login(loginViewModel: LoginViewModel) {
        val value = GlobalScope.async {
            OMGApi.retrofitService.login(loginViewModel)
        }
        Util.saveToken(activity, value.await())
    }
}
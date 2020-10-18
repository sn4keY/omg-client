package com.norbertneudert.openmygarage.service

import android.app.Activity
import android.util.Log
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.*

class ApiHandlerAuthentication(private val activity: Activity) {
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    suspend fun login(loginViewModel: LoginViewModel) {
        val value = GlobalScope.async {
            OMGApi.retrofitService.login(loginViewModel)
        }
        Util.saveToken(activity, value.await())
    }

    private suspend fun onLogin(loginViewModel: LoginViewModel) {
        val tokenViewModel = OMGApi.retrofitService.login(loginViewModel)
        Util.saveToken(activity, tokenViewModel)
        Log.i("ApiHandlerAuthentication", "Token saved")
    }
}
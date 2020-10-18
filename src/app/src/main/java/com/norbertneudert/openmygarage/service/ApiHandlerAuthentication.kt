package com.norbertneudert.openmygarage.service

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ApiHandlerAuthentication(private val activity: Activity) {
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    fun login(loginViewModel: LoginViewModel) {
        coroutineScope.launch {
            onLogin(loginViewModel)
        }
    }

    private suspend fun onLogin(loginViewModel: LoginViewModel) {
        val tokenViewModel = OMGApi.retrofitService.login(loginViewModel)
        Util.saveToken(activity, tokenViewModel)
    }
}
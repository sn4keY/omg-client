package com.norbertneudert.openmygarage.service

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
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
        saveToken(tokenViewModel)
    }

    private fun saveToken(tokenViewModel: TokenViewModel?) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with( sharedPref.edit() ) {
            putString("token", tokenViewModel?.token)
            putString("expiration", tokenViewModel?.expiration)
            apply()
        }
    }
}
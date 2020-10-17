package com.norbertneudert.openmygarage.service

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ApiHandlerAuthentication(private val activity: Activity) {
    private var handlerJob = Job()
    private val coroutineScope = CoroutineScope(handlerJob + Dispatchers.Main)

    fun login(loginViewModel: LoginViewModel) {
        coroutineScope.launch {
            val tokenViewModelDeferred = OMGApi.retrofitService.login(loginViewModel)
            try {
                val tokenViewModel = tokenViewModelDeferred.await()
                saveToken(tokenViewModel)
            } catch (e: Exception) {

            }
        }
    }

    private fun saveToken(tokenViewModel: TokenViewModel) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with( sharedPref.edit() ) {
            putString("token", tokenViewModel.token)
            putLong("expiration", tokenViewModel.expiration.time)
            apply()
        }
    }
}
package com.norbertneudert.openmygarage.service.auth

import com.norbertneudert.openmygarage.service.OMGApi
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.*

class ApiHandlerAuthentication() {
    companion object {
        @Volatile
        private var INSTANCE: ApiHandlerAuthentication? = null

        fun getInstance() : ApiHandlerAuthentication {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = ApiHandlerAuthentication()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    suspend fun login(loginViewModel: LoginViewModel) {
        val value = GlobalScope.async {
            OMGApi.retrofitService.login(loginViewModel)
        }
        Util.getInstance().saveToken(value.await())
    }
}
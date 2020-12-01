package com.norbertneudert.openmygarage.service.auth

import android.widget.Toast
import com.norbertneudert.openmygarage.util.Util
import okhttp3.*
import kotlin.coroutines.coroutineContext

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        var response = chain.proceed(newRequest)
        if (response.code() == 403) {
            response = response.newBuilder().code(200).build()
            Util.getInstance().insufficientPermission()
        }
        return response
    }

    private fun Request.signedRequest(): Request {
        val token = Util.getInstance().getToken()
        return newBuilder()
            .header("Authorization", "Bearer ${token?.token}")
            .build()
    }
}
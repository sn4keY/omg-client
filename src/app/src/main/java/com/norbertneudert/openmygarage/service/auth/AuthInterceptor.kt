package com.norbertneudert.openmygarage.service.auth

import com.norbertneudert.openmygarage.util.Util
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {
        val token = Util.getInstance().getToken()
        return newBuilder()
            .header("Authorization", "Bearer ${token?.token}")
            .build()
    }
}
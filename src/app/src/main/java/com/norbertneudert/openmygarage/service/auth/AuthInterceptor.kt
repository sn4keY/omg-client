package com.norbertneudert.openmygarage.service.auth

import com.norbertneudert.openmygarage.util.Util
import okhttp3.*

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        val response = chain.proceed(newRequest)
        if (response.code() == 401) {
            return response.newBuilder().code(200).body(ResponseBody.create(MediaType.get("application/json"),"{\n" +
                    "    \"token\": \"\",\n" +
                    "    \"expiration\": 0\n" +
                    "}")).build()
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
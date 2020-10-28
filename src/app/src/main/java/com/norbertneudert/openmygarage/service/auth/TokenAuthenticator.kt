package com.norbertneudert.openmygarage.service.auth

import android.util.Log
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import com.norbertneudert.openmygarage.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 2 -> null // TODO (nincs jog)
        else -> response.createSignedRequest()
    }

    fun Request.signWithToken(tokenViewModel: TokenViewModel) =
        newBuilder()
            .header("Authorization", "Bearer ${tokenViewModel.token}")
            .build()

    private val Response.retryCount: Int
        get() {
            var currentResponse = priorResponse()
            var result = 0
            while (currentResponse != null) {
                result++
                currentResponse = currentResponse.priorResponse()
            }
            return result
        }

    private fun Response.createSignedRequest(): Request? = try {
        val token = Util.getInstance().getToken()!!
        request().signWithToken(token)
    } catch (error: Throwable) {
        Log.i("Authenticator", error.message!!)
        null // TODO (crash)
    }
}
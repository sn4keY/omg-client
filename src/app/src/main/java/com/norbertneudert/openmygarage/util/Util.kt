package com.norbertneudert.openmygarage.util

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.LoginViewModel
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import java.text.SimpleDateFormat

class Util {
    private var context: Context? = null

    companion object {
        @Volatile
        private var INSTANCE: Util? = null

        fun getInstance(): Util {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Util()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun saveToken(tokenViewModel: TokenViewModel) {
        val sharedPref = context?.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
        with( sharedPref!!.edit() ) {
            putString("token", tokenViewModel.token)
            putString("expiration", tokenViewModel.expiration)
            apply()
        }
    }

    fun getToken() : TokenViewModel? {
        val sharedPref = context?.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
        val token = sharedPref?.getString("token", "")!!
        val expiration = sharedPref.getString("expiration", "")!!

        return TokenViewModel(token, expiration)
    }

    fun destroyToken() {
        val sharedPref = context?.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
        with( sharedPref!!.edit() ) {
            putString("token", "")
            putString("expiration", "")
            apply()
        }
    }

    fun getTokenExpirationInLong(activity: Activity) : Long {
        val epochMicrotimeDiff = 621355968000000000L
        val sharedPref = activity.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
        val expiration = sharedPref.getString("expiration", "")!!.toLong()
        return (expiration - epochMicrotimeDiff) / 10000
    }

    fun getFormattedDateFromLong(time: Long) : String {
        val epochMicrotimeDiff = 621355968000000000L
        val dateTime = (time-epochMicrotimeDiff) / 10000
        return SimpleDateFormat("yyyy.MM.dd HH:mm").format(dateTime)
    }

    fun saveLogin(loginViewModel: LoginViewModel) {
        val sharedPref = context?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        with(sharedPref!!.edit()) {
            putString("username", loginViewModel.username)
            putString("password", loginViewModel.password)
            apply()
        }
    }

    fun getLogin() : LoginViewModel? {
        val sharedPref = context?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")!!
        val password = sharedPref.getString("password", "")!!

        return LoginViewModel(username, password)
    }

    fun destroyLogin() {
        val sharedPref = context?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        with(sharedPref!!.edit()) {
            putString("username", "")
            putString("password", "")
            apply()
        }
    }
}
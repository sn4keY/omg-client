package com.norbertneudert.openmygarage.util

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.TokenViewModel

class Util {
    companion object {
        fun saveToken(activity: Activity, tokenViewModel: TokenViewModel) {
            val sharedPref = activity.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
            with( sharedPref.edit() ) {
                putString("token", tokenViewModel.token)
                putString("expiration", tokenViewModel.expiration)
                apply()
            }
        }

        fun getToken(activity: Activity) : TokenViewModel? {
            val sharedPref = activity.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "")!!
            val expiration = sharedPref.getString("expiration", "")!!

            return TokenViewModel(token, expiration)
        }

        fun destroyToken(activity: Activity) {
            val sharedPref = activity.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
            with( sharedPref.edit() ) {
                putString("token", "")
                putString("expiration", "")
                apply()
            }
        }
    }
}
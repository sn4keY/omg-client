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
            val token = sharedPref.getString("token", null)!!
            val expiration = sharedPref.getString("expiration", null)!!

            return TokenViewModel(token, expiration)
        }
    }
}
package com.norbertneudert.openmygarage.util

import android.app.Activity
import android.content.Context
import com.norbertneudert.openmygarage.service.models.TokenViewModel
import java.text.SimpleDateFormat

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
    }
}
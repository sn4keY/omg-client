package com.norbertneudert.openmygarage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.norbertneudert.openmygarage.service.models.TokenViewModel

class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent: Intent

        val token = getToken()
        if (token != null) {
            activityIntent = Intent(this.applicationContext, MainActivity::class.java)
        } else {
            activityIntent = Intent(this.applicationContext, LoginActivity::class.java)
        }

        startActivity(activityIntent)
        finish()
    }

    private fun getToken() : TokenViewModel? {
        val tokenSharedPref = this.getSharedPreferences("API_TOKEN", Context.MODE_PRIVATE)
        val token = tokenSharedPref.getString("token", null)!!
        val expiration = tokenSharedPref.getString("expiration", null)!!

        if (token.isEmpty())
            return null

        return TokenViewModel(token, expiration)
    }
}
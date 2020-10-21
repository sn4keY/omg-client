package com.norbertneudert.openmygarage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.norbertneudert.openmygarage.util.Util

class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent: Intent

        val token = Util.getToken(this)
        activityIntent = if (token?.token!!.isNotEmpty()) {
            Intent(this.applicationContext, MainActivity::class.java)
        } else {
            Intent(this.applicationContext, LoginActivity::class.java)
        }

        startActivity(activityIntent)
        finish()
    }
}
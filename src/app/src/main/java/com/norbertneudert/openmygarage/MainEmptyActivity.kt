package com.norbertneudert.openmygarage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.norbertneudert.openmygarage.util.Util

class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent: Intent
        val util = Util.getInstance()
        util.setContext(applicationContext)
        clearSharedPreferences()

        val token = util.getToken()
        activityIntent = if (token?.token!!.isNotEmpty()) {
            Intent(this.applicationContext, MainActivity::class.java)
        } else {
            Intent(this.applicationContext, LoginActivity::class.java)
        }

        createNotificationChannel()
        FirebaseMessaging.getInstance().subscribeToTopic("news")

        startActivity(activityIntent)
        finish()
    }

    private fun clearSharedPreferences() {
        Util.getInstance().destroyToken()
        Util.getInstance().destroyLogin()
    }

    private fun createNotificationChannel() {
        val id = getString(R.string.omg_notification_channel_id)
        val name = getString(R.string.omg_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
package com.norbertneudert.openmygarage.service

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.norbertneudert.openmygarage.R
import com.norbertneudert.openmygarage.util.NotificationID

class NotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it)
        }
    }

    private fun sendRegistrationToServer(token: String?){
        // TODO("backend save token")
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        val id = getString(R.string.omg_notification_channel_id)
        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(NotificationID.getID(), builder.build())
        }
    }
}
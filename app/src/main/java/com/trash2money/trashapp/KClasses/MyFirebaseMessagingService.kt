package com.trash2money.trashapp.KClasses

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "FCM Service"

    @SuppressLint("ObsoleteSdkInt")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, HomeActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(message.data["title"])
                .setContentText(message.data["msg"])
                .setSmallIcon(R.drawable.ic_log_notification).setAutoCancel(true)
                .setContentIntent(pendingIntent).build()

        notificationManager.notify(notificationID, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "chanelName"
        val channel = NotificationChannel(CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH).apply {
            description = "My Channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {

        Log.d(TAG, "Refreshed token : $token")
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "getInstanceID failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result?.token
            Log.d(TAG, token)
            Toast.makeText(baseContext, token, Toast.LENGTH_LONG).show()

        }
    }
}
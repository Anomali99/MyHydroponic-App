package id.my.anomali99.myhydroponic.data.remote.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

import id.my.anomali99.myhydroponic.R
import id.my.anomali99.myhydroponic.domain.repository.MainRepository

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var repository: MainRepository

    companion object {
        const val TANK_WARNING = "tank_warning"
        const val DEVICE_WARNING = "device_warning"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.notification != null){
            val title = message.notification?.title
            val body = message.notification?.body
            Log.d("FCM", "Title: $title, Body: $body")
            if (title != null && body != null) {
                startNotification(title, body)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Create new token: $token")

        CoroutineScope(Dispatchers.IO).launch {
            repository.saveLocalFcmToken(token)
        }
    }

    private fun startNotification(title: String, body: String) {
        val channelId = when (title) {
            "Tank Warning" -> TANK_WARNING
            "Device Warning" -> DEVICE_WARNING
            else -> TANK_WARNING
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(Random(1).nextInt(), builder.build())
    }

    private fun createNotificationChannels() {
        val taskChannel = NotificationChannel(
            TANK_WARNING,
            "Tank Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        val plantChannel = NotificationChannel(
            DEVICE_WARNING,
            "Device Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(taskChannel)
        notificationManager.createNotificationChannel(plantChannel)
    }

}
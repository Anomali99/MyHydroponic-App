package id.my.anomali99.myhydroponic.data.remote.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FcmManager @Inject constructor() {

    private val fcm = FirebaseMessaging.getInstance()

    suspend fun getFreshToken(): String? {
        return try {
            fcm.token.await()
        } catch (e: Exception) {
            Log.e("FcmManager", "Error getting FCM token", e)
            null
        }
    }


    suspend fun subscribeToTopic(): Boolean {
        return try {
            fcm.subscribeToTopic("notification").await()
            Log.d("FcmManager", "Subscribed to notification")
            true
        } catch (e: Exception) {
            Log.e("FcmManager", "Failed to subscribe to notification", e)
            false
        }
    }


    suspend fun unsubscribeFromTopic(): Boolean {
        return try {
            fcm.unsubscribeFromTopic("notification").await()
            Log.d("FcmManager", "Subscribed to notification")
            true
        } catch (e: Exception) {
            Log.e("FcmManager", "Failed to subscribe to notification", e)
            false
        }
    }
}
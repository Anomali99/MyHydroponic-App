package id.my.anomali99.myhydroponic.domain.repository

import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun isSubscribe(): Flow<Boolean>

    suspend fun setIsSubscribe(status: Boolean)

    fun getLocalFcmToken(): Flow<String?>

    suspend fun saveLocalFcmToken(token: String)

    suspend fun getRemoteFcmToken(): String?

    suspend fun subscribeToTopic(): Boolean

    suspend fun unsubscribeFromTopic(): Boolean
}
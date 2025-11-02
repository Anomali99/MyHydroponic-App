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

    fun getDuration(): Flow<Float>
    suspend fun setDuration(duration: Float)

    fun getMaxMain(): Flow<Float>
    suspend fun setMaxMain(level: Float)

    fun getMaxNutrientA(): Flow<Float>
    suspend fun setMaxNutrientA(level: Float)

    fun getMaxNutrientB(): Flow<Float>
    suspend fun setMaxNutrientB(level: Float)

    fun getMaxPhUp(): Flow<Float>
    suspend fun setMaxPhUp(level: Float)

    fun getMaxPhDown(): Flow<Float>
    suspend fun setMaxPhDown(level: Float)

}
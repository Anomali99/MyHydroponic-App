package id.my.anomali99.myhydroponic.data.repository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

import id.my.anomali99.myhydroponic.data.local.datastore.SettingsDataStore
import id.my.anomali99.myhydroponic.data.remote.fcm.FcmManager
import id.my.anomali99.myhydroponic.domain.repository.MainRepository

class MainRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val fcmManager: FcmManager
) : MainRepository {

    override fun isSubscribe(): Flow<Boolean> {
        return settingsDataStore.subscribeFlow
    }

    override suspend fun setIsSubscribe(status: Boolean) {
        settingsDataStore.saveSubscribe(status)
    }

    override fun getApiToken(): Flow<String> {
        return settingsDataStore.apiTokenFlow
    }

    override suspend fun saveApiToken(token: String) {
        return settingsDataStore.saveApiToken(token)
    }

    override fun getLocalFcmToken(): Flow<String?> {
        return settingsDataStore.fcmTokenFlow
    }

    override suspend fun saveLocalFcmToken(token: String) {
        settingsDataStore.saveFcmToken(token)
    }

    override suspend fun getRemoteFcmToken(): String? {
        return fcmManager.getFreshToken()
    }

    override suspend fun subscribeToTopic(): Boolean {
        return fcmManager.subscribeToTopic()
    }

    override suspend fun unsubscribeFromTopic(): Boolean {
        return fcmManager.unsubscribeFromTopic()
    }

    override suspend fun setDuration(duration: Float) {
        settingsDataStore.saveDuration(duration)
    }

    override fun getDuration(): Flow<Float> {
        return settingsDataStore.durationFlow
    }
}
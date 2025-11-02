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

    override suspend fun setMaxPhUp(level: Float) {
        settingsDataStore.saveMaxPhUp(level)
    }

    override fun getMaxPhUp(): Flow<Float> {
        return settingsDataStore.maxPhUpFlow
    }

    override suspend fun setMaxPhDown(level: Float) {
        settingsDataStore.saveMaxPhDown(level)
    }

    override fun getMaxPhDown(): Flow<Float> {
        return settingsDataStore.maxPhDownFlow
    }

    override suspend fun setMaxNutrientA(level: Float) {
        settingsDataStore.saveMaxNutrientA(level)
    }

    override fun getMaxNutrientA(): Flow<Float> {
        return settingsDataStore.maxNutrientAFlow
    }


    override suspend fun setMaxNutrientB(level: Float) {
        settingsDataStore.saveMaxNutrientB(level)
    }

    override fun getMaxNutrientB(): Flow<Float> {
        return settingsDataStore.maxNutrientBFlow
    }
    override suspend fun setMaxMain(level: Float) {
        settingsDataStore.saveMaxMain(level)
    }

    override fun getMaxMain(): Flow<Float> {
        return settingsDataStore.maxMainFlow
    }

    override suspend fun setDuration(duration: Float) {
        settingsDataStore.saveDuration(duration)
    }

    override fun getDuration(): Flow<Float> {
        return settingsDataStore.durationFlow
    }
}
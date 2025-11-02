package id.my.anomali99.myhydroponic.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        val SUBSCRIBE = booleanPreferencesKey("subscribe")
        val DURATION = floatPreferencesKey("duration")
        val MAX_MAIN = floatPreferencesKey("max_main")
        val MAX_NUTRIENT_A = floatPreferencesKey("max_nutrient_a")
        val MAX_NUTRIENT_B = floatPreferencesKey("max_nutrient_b")
        val MAX_PH_UP = floatPreferencesKey("max_ph_up")
        val MAX_PH_DOWN = floatPreferencesKey("max_ph_down")
    }

    suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN] = token
        }
    }

    val fcmTokenFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN]
        }

    suspend fun saveSubscribe(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SUBSCRIBE] = status
        }
    }

    val subscribeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SUBSCRIBE] ?: false
        }

    suspend fun saveDuration(duration: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DURATION] = duration
        }
    }

    val durationFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DURATION] ?: 1f
        }

    suspend fun saveMaxMain(level: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_MAIN] = level
        }
    }

    val maxMainFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_MAIN] ?: 20f
        }

    suspend fun saveMaxNutrientA(level: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_NUTRIENT_A] = level
        }
    }

    val maxNutrientAFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_NUTRIENT_A] ?: 20f
        }

    suspend fun saveMaxNutrientB(level: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_NUTRIENT_B] = level
        }
    }

    val maxNutrientBFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_NUTRIENT_B] ?: 20f
        }

    suspend fun saveMaxPhUp(level: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_PH_UP] = level
        }
    }

    val maxPhUpFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_PH_UP] ?: 20f
        }

    suspend fun saveMaxPhDown(level: Float) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_PH_DOWN] = level
        }
    }

    val maxPhDownFlow: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_PH_DOWN] ?: 20f
        }
}
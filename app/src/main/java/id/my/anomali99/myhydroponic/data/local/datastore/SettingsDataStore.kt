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
        val API_TOKEN = stringPreferencesKey("api_token")
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
        val SUBSCRIBE = booleanPreferencesKey("subscribe")
        val DURATION = floatPreferencesKey("duration")
    }

    suspend fun saveApiToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_TOKEN] = token
        }
    }

    val apiTokenFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.API_TOKEN] ?: "7125"
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
            preferences[PreferencesKeys.DURATION] ?: 0.5f
        }

}
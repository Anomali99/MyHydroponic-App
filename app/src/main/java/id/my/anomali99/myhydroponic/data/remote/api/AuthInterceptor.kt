package id.my.anomali99.myhydroponic.data.remote.api

import id.my.anomali99.myhydroponic.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            settingsDataStore.apiTokenFlow.first()
        }

        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        requestBuilder.addHeader("Authorization", "Bearer $token")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
package id.my.anomali99.myhydroponic.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

import id.my.anomali99.myhydroponic.domain.repository.MainRepository

class ManageTopicSubscriptionUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend fun subscribe(): Boolean {
        var token = repository.getLocalFcmToken().firstOrNull()

        if (token.isNullOrBlank()) {
            Log.d("UseCase", "Local token is empty, get new token...")
            token = repository.getRemoteFcmToken()

            if (token != null) {
                repository.saveLocalFcmToken(token)
                Log.d("UseCase", "New token saved.")
            } else {
                Log.e("UseCase", "Failed to get new token.")
                return false
            }
        } else {
            Log.d("UseCase", "Use local token is exists.")
        }

        val isSucces = repository.subscribeToTopic()
        repository.setIsSubscribe(isSucces)
        return isSucces
    }

    suspend fun unsubscribe(): Boolean {
        repository.setIsSubscribe(false)
        return repository.unsubscribeFromTopic()
    }

    suspend fun isSubscribe(): Boolean {
        return repository.isSubscribe().first()
    }
}
package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.domain.repository.MainRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ManageSettingsDataUseCase @Inject constructor(
    private val repository: MainRepository) {

    suspend fun getApiToken(): String{
        return repository.getApiToken().first()
    }

    suspend fun setApiToken(token: String) {
        repository.saveApiToken(token)
    }

    suspend fun getDuration(): Float{
        return repository.getDuration().first()
    }

    suspend fun setDuration(duration: Float) {
        repository.setDuration(duration)
    }
}
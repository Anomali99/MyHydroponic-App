package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.domain.repository.MainRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ManageSettingsDataUseCase @Inject constructor(
    private val repository: MainRepository) {

    suspend fun getDuration(): Float{
        return repository.getDuration().first()
    }

    suspend fun setDuration(duration: Float) {
        repository.setDuration(duration)
    }

    suspend fun getMaxMain(): Float{
        return repository.getMaxMain().first()
    }

    suspend fun setMaxMain(level: Float){
        repository.setMaxMain(level)
    }

    suspend fun getMaxPhUp(): Float{
        return repository.getMaxPhUp().first()
    }

    suspend fun setMaxPhUp(level: Float){
        repository.setMaxPhUp(level)
    }

    suspend fun getMaxPhDown(): Float{
        return repository.getMaxPhDown().first()
    }

    suspend fun setMaxPhDown(level: Float){
        repository.setMaxPhDown(level)
    }

    suspend fun getMaxNutrientA(): Float{
        return repository.getMaxNutrientA().first()
    }

    suspend fun setMaxNutrientA(level: Float){
        repository.setMaxNutrientA(level)
    }

    suspend fun getMaxNutrientB(): Float{
        return repository.getMaxNutrientB().first()
    }

    suspend fun setMaxNutrientB(level: Float){
        repository.setMaxNutrientB(level)
    }
}
package id.my.anomali99.myhydroponic.domain.usecase

import javax.inject.Inject

import id.my.anomali99.myhydroponic.data.model.ThresholdModel
import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository

class UpdateThresholdUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: ThresholdModel) = repository.updateThresholds(settings)
}

package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository
import javax.inject.Inject

class GetThresholdUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getThresholds()
}
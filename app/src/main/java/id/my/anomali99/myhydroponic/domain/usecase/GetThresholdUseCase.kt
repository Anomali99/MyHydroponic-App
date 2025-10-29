package id.my.anomali99.myhydroponic.domain.usecase

import javax.inject.Inject

import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository

class GetThresholdUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getThresholds()
}
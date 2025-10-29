package id.my.anomali99.myhydroponic.domain.usecase

import javax.inject.Inject

import id.my.anomali99.myhydroponic.domain.repository.MqttRepository

class StartMqttConnectionUseCase @Inject constructor(
    private val mqttRepository: MqttRepository
) {
    suspend operator fun invoke() {
        mqttRepository.connectAndSubscribe()
    }
}
package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.domain.repository.MqttRepository
import javax.inject.Inject


class StartMqttConnectionUseCase @Inject constructor(
    private val mqttRepository: MqttRepository
) {
    suspend operator fun invoke() {
        mqttRepository.connectAndSubscribe()
    }
}
package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.domain.repository.MqttRepository
import javax.inject.Inject

class SendToMqttTopicUseCase @Inject constructor(
    private val mqttRepository: MqttRepository
) {
    suspend operator fun invoke(topic: String, command: String) {
        mqttRepository.sendMqttCommand(
            topic = topic,
            message = command
        )
    }
}
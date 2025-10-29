package id.my.anomali99.myhydroponic.domain.usecase

import id.my.anomali99.myhydroponic.data.model.EnvironmentModel
import id.my.anomali99.myhydroponic.domain.repository.MqttRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SubscribeToMqttDataUseCase @Inject constructor(
    private val mqttRepository: MqttRepository
) {
    operator fun invoke(): Flow<EnvironmentModel> {
        return mqttRepository.getMqttDataFlow()
    }
}

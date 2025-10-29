package id.my.anomali99.myhydroponic.domain.repository

import id.my.anomali99.myhydroponic.data.model.EnvironmentModel
import kotlinx.coroutines.flow.Flow

interface MqttRepository {

    suspend fun connectAndSubscribe()

    fun getMqttDataFlow(): Flow<EnvironmentModel>

    suspend fun sendMqttCommand(topic: String, message: String)

    fun disconnect()
}
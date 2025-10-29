package id.my.anomali99.myhydroponic.domain.repository

import kotlinx.coroutines.flow.Flow

import id.my.anomali99.myhydroponic.data.model.EnvironmentModel

interface MqttRepository {

    suspend fun connectAndSubscribe()

    fun getMqttDataFlow(): Flow<EnvironmentModel>

    suspend fun sendMqttCommand(topic: String, message: String)

    fun disconnect()
}
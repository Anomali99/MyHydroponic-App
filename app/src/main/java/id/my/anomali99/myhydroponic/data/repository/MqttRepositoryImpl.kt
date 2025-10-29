package id.my.anomali99.myhydroponic.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

import id.my.anomali99.myhydroponic.data.model.EnvironmentModel
import id.my.anomali99.myhydroponic.data.remote.mqtt.MqttClientWrapper
import id.my.anomali99.myhydroponic.domain.repository.MqttRepository
import id.my.anomali99.myhydroponic.utils.Constants

@Singleton
class MqttRepositoryImpl @Inject constructor(
    private val mqttClient: MqttClientWrapper,
    private val gson: Gson
) : MqttRepository {

    override suspend fun connectAndSubscribe() {
        val wsUri = Constants.MQTT_URI
        val dataTopic = Constants.ENV_DATA

        val clientId = "android-client-${System.currentTimeMillis()}"
        mqttClient.connect(wsUri, clientId)

        mqttClient.subscribe(dataTopic, qos = 1)
    }

    override fun getMqttDataFlow(): Flow<EnvironmentModel> {
        val dataTopic = Constants.ENV_DATA

        return mqttClient.messages
            .filter { (topic, payload) ->
                topic == dataTopic && payload.isNotBlank()
            }
            .map { (topic, payload) -> payload }
            .map { rawJsonPayload ->
                parseJsonToSensorData(rawJsonPayload)
            }
    }

    override suspend fun sendMqttCommand(topic: String, message: String) {
        mqttClient.publish(topic, message, qos = 1)
    }

    override fun disconnect() {
        mqttClient.disconnect()
    }


    private fun parseJsonToSensorData(payload: String): EnvironmentModel {
        return try {
            gson.fromJson(payload, EnvironmentModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            EnvironmentModel(
                ph = 0f,
                tds = 0f,
                aTank = 0f,
                bTank = 0f,
                phUpTank = 0f,
                phDownTank = 0f,
                mainTank = 0f,
                datetime = "2001-01-01 00:00:00"
            )
        }
    }
}
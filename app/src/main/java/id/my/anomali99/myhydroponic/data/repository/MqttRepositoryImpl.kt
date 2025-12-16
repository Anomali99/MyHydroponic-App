package id.my.anomali99.myhydroponic.data.repository

import android.util.Log
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
        val username = Constants.MQTT_USER
        val password = Constants.MQTT_PASS
        val dataTopic = Constants.ENV_DATA
        val deviceStatus = Constants.DEVICE_STATUS

        Log.d("MqttRepo", "Starting connection... Target Topic: $dataTopic")

        val clientId = "android-client-${System.currentTimeMillis()}"
        mqttClient.connect(wsUri, clientId, username, password)

        mqttClient.subscribe(dataTopic, qos = 1)
        mqttClient.subscribe(deviceStatus, qos = 1)
    }

    override fun getMqttDataFlow(): Flow<EnvironmentModel> {
        val dataTopic = Constants.ENV_DATA

        return mqttClient.messages
            .filter { (topic, payload) ->
                if (topic == dataTopic) {
                    Log.d("MqttRepo", "FILTER PASS: Payload received from $topic")
                    payload.isNotBlank()
                } else {
                    if (topic != Constants.DEVICE_STATUS && topic != "CONNECTION_LOST" && topic != "CONNECTION_ERROR") {
                        Log.d("MqttRepo", "FILTER REJECT: Topic '$topic' is not equal to '$dataTopic'")
                    }
                    false
                }
            }
            .map { (_, payload) -> payload }
            .map { rawJsonPayload ->
                parseJsonToSensorData(rawJsonPayload)
            }
    }

    override fun getDeviceStatus(): Flow<Boolean> {
        val deviceStatus = Constants.DEVICE_STATUS

        return mqttClient.messages
            .filter { (topic, payload ) ->
                topic == deviceStatus && payload.isNotBlank()
            }
            .map { (_, payload) ->
                Log.d("MqttRepo", "Device Status: $payload")
                payload.trim() == "1"
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
            val data = gson.fromJson(payload, EnvironmentModel::class.java)
            Log.d("MqttRepo", "JSON Parsing Successful! pH: ${data.ph}, Temp: ${data.temp}")
            data
        } catch (e: Exception) {
            Log.e("MqttRepo", "JSON PARSING ERROR: ${e.message}")
            Log.e("MqttRepo", "Payload causing error: $payload")
            e.printStackTrace()

            EnvironmentModel(
                ph = 0f,
                tds = 0f,
                temp = 0f,
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
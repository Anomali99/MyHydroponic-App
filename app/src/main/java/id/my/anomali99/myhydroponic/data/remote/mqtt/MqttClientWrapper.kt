package id.my.anomali99.myhydroponic.data.remote.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttClientWrapper @Inject constructor(
    private val context: Context
) {

    private val _messages = MutableSharedFlow<Pair<String, String>>(replay = 1)
    val messages = _messages.asSharedFlow()

    private var client: MqttClient? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val mqttCallback = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            Log.e("MqttClient", "Connection Lost: ${cause?.message}")
            coroutineScope.launch {
                _messages.emit("CONNECTION_LOST" to (cause?.message ?: "Unknown error"))
            }
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            if (topic != null && message != null) {
                val payload = message.toString()
                Log.d("MqttClient", "MESSAGE ARRIVED! Topic: $topic, Payload: $payload")
                coroutineScope.launch {
                    _messages.emit(topic to payload)
                }
            }
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d("MqttClient", "Delivery Complete")
        }
    }


    fun connect(serverUri: String, clientId: String, username: String, password: String) {
        if (client?.isConnected == true) {
            Log.d("MqttClient", "The client has been connected previously")
            return
        }

        try {
            val persistence = MqttDefaultFilePersistence(context.cacheDir.absolutePath)
            client = MqttClient(serverUri, clientId, persistence)
            client?.setCallback(mqttCallback)

            val options = MqttConnectOptions().apply {
                this.userName = username
                this.password = password.toCharArray()
                this.isAutomaticReconnect = true
                this.isCleanSession = true
                this.connectionTimeout = 10
                this.keepAliveInterval = 60
            }

            Log.d("MqttClient", "Connecting to $serverUri as $clientId...")
            client?.connect(options)
            Log.d("MqttClient", "SUCCESSFULLY CONNECTED to MQTT Broker!")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MqttClient", "FAILED TO CONNECT: ${e.message}")
            coroutineScope.launch {
                _messages.emit("CONNECTION_ERROR" to (e.message ?: "Failed to connect"))
            }
        }
    }

    fun subscribe(topic: String, qos: Int = 1) {
        try {
            if (client?.isConnected == true) {
                client?.subscribe(topic, qos)
                Log.d("MqttClient", "Successfully Subscribed to topic: $topic")
            } else {
                Log.e("MqttClient", "Failed to Subscribe: Client is not connected yet")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MqttClient", "Error when subscribing: ${e.message}")
        }
    }

    fun publish(topic: String, payload: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage(payload.toByteArray())
            message.qos = qos
            message.isRetained = retained
            client?.publish(topic, message)
            Log.d("MqttClient", "Publish to $topic: $payload")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MqttClient", "Error when Publish: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
            client = null
            Log.d("MqttClient", "Disconnected")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
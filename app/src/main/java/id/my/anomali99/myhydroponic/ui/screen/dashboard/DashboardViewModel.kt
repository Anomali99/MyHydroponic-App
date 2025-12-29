package id.my.anomali99.myhydroponic.ui.screen.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

import id.my.anomali99.myhydroponic.domain.usecase.ManageSettingsDataUseCase
import id.my.anomali99.myhydroponic.domain.usecase.SendToMqttTopicUseCase
import id.my.anomali99.myhydroponic.domain.usecase.StartMqttConnectionUseCase
import id.my.anomali99.myhydroponic.domain.usecase.SubscribeToMqttDataUseCase
import id.my.anomali99.myhydroponic.domain.usecase.SubscribeToMqttDeviceUseCase
import id.my.anomali99.myhydroponic.utils.Constants
import kotlinx.coroutines.delay

data class DashboardUiState(
    val isLoading: Boolean = false,
    val isMqttConnected: Boolean = false,
    val deviceIsActive: Boolean = false,
    val ph: String = "0.0",
    val tds: String = "0",
    val temp: String = "0.0",
    val mainTank: Float = 0f,
    val aTank: Float = 0f,
    val bTank: Float = 0f,
    val phUpTank: Float = 0f,
    val phDownTank: Float = 0f,
    val datetime: String = "01 Januari 2001 00:00 WIB",
    val duration: Float = 0.5f,
    val errorMessage: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val startMqttConnectionUseCase: StartMqttConnectionUseCase,
    private val subscribeToMqttDataUseCase: SubscribeToMqttDataUseCase,
    private val subscribeToMqttDeviceUseCase: SubscribeToMqttDeviceUseCase,
    private val sendToMqttTopicUseCase: SendToMqttTopicUseCase,
    private val manageSettingsDataUseCase: ManageSettingsDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        startMqttConnection()
        collectMqttData()
        collectDeviceStatus()

    }

    private fun formatValue(value: Float): String {
        val localeID = Locale("id", "ID")
        return String.format(localeID, "%.2f", value)
            .trimEnd('0')
            .trimEnd(',')
    }

    fun loadSettings() {
        viewModelScope.launch {
            val duration = manageSettingsDataUseCase.getDuration()

            _uiState.update {
                it.copy(
                    duration = duration
                )
            }
        }
    }

    private fun startMqttConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                startMqttConnectionUseCase()
                _uiState.update { it.copy(isMqttConnected = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isMqttConnected = false,
                        errorMessage = "Koneksi MQTT gagal: ${e.message}"
                    )
                }
            }
        }
    }

    private fun collectDeviceStatus() {
        viewModelScope.launch {
            subscribeToMqttDeviceUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error data stream: ${e.message}"
                        )
                    }
                }
                .collect { status ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            deviceIsActive = status
                        )
                    }
                }
        }
    }

    private fun collectMqttData() {
        viewModelScope.launch {
            subscribeToMqttDataUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error data stream: ${e.message}"
                        )
                    }
                }
                .collect { newData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ph = formatValue(newData.ph),
                            tds = newData.tds.toInt().toString(),
                            temp = formatValue(newData.temp),
                            mainTank = newData.mainTank,
                            phUpTank = newData.phUpTank,
                            phDownTank = newData.phDownTank,
                            aTank = newData.aTank,
                            bTank = newData.bTank,
                            datetime = parseDatetime(newData.datetime)
                        )
                    }
                }
        }
    }

    private fun parseDatetime(inputString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(inputString, inputFormatter)

        val wibZoneId = ZoneId.of("Asia/Jakarta")
        val zonedDateTime = localDateTime.atZone(wibZoneId)

        val indonesiaLocale = Locale("id", "ID")
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm z", indonesiaLocale)

        return zonedDateTime.format(outputFormatter)
    }

    fun reloadDashboard() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                startMqttConnectionUseCase()

                val duration = manageSettingsDataUseCase.getDuration()
                _uiState.update { it.copy(duration = duration) }

                delay(3000)

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(errorMessage = "Failed to reload: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRefreshClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                sendToMqttTopicUseCase(
                    Constants.ENV_REFRESH,
                    command = "1"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal mengirim perintah: ${e.message}") }
            }
        }
    }

    fun onAddNutritionClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                sendToMqttTopicUseCase(
                    Constants.PUMP_MANUALLY,
                    command = "{ \"pump\": \"NUTRITION\", \"duration\": ${_uiState.value.duration}}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(errorMessage = "Gagal mengirim perintah: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAddPhUpClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                sendToMqttTopicUseCase(
                    Constants.PUMP_MANUALLY,
                    command = "{ \"pump\": \"PH_UP\", \"duration\": ${_uiState.value.duration}}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(errorMessage = "Gagal mengirim perintah: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAddPhDownClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                sendToMqttTopicUseCase(
                    Constants.PUMP_MANUALLY,
                    command = "{ \"pump\": \"PH_DOWN\", \"duration\": ${_uiState.value.duration}}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(errorMessage = "Gagal mengirim perintah: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}
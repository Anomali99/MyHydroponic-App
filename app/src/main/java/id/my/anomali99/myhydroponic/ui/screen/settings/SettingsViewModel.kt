package id.my.anomali99.myhydroponic.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import id.my.anomali99.myhydroponic.data.model.ThresholdModel
import id.my.anomali99.myhydroponic.domain.usecase.GetThresholdUseCase
import id.my.anomali99.myhydroponic.domain.usecase.ManageTopicSubscriptionUseCase
import id.my.anomali99.myhydroponic.domain.usecase.ManageSettingsDataUseCase
import id.my.anomali99.myhydroponic.domain.usecase.UpdateThresholdUseCase
import id.my.anomali99.myhydroponic.utils.Resource

data class SettingsUiState(
    val isLoading: Boolean = false,
    val notificationEnabled: Boolean = true,
    val thresholdEnabled: Boolean = false,
    val phMin: String = "6.0",
    val phMax: String = "7.0",
    val tdsMin: String = "800",
    val tdsMax: String = "1200",
    val duration: String = "1.0",
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

data class OtherModel(
    val duration: String,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThresholdsUseCase: GetThresholdUseCase,
    private val updateThresholdsUseCase: UpdateThresholdUseCase,
    private val manageTopicSubscriptionUseCase: ManageTopicSubscriptionUseCase,
    private val manageSettingsDataUseCase: ManageSettingsDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var originalSettings: ThresholdModel? = null
    private var originalOtherSettings: OtherModel? = null

    init {
        loadSettings()
    }

    fun loadSettings() {
        getThresholdsUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    val settings = resource.data!!
                    originalSettings = settings
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            thresholdEnabled = settings.thresholdEnabled,
                            phMin = settings.phMin,
                            phMax = settings.phMax,
                            tdsMin = settings.tdsMin,
                            tdsMax = settings.tdsMax
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                }
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val isSubscribed = manageTopicSubscriptionUseCase.isSubscribe()
            val duration = manageSettingsDataUseCase.getDuration().toString()

            _uiState.update {
                it.copy(
                    notificationEnabled = isSubscribed,
                    duration = duration
                )
            }

            originalOtherSettings = OtherModel(
                duration = duration
            )
        }
    }

    fun onNotificationEnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            var isSuccess = false

            isSuccess = if (enabled){
                manageTopicSubscriptionUseCase.subscribe()
            } else {
                manageTopicSubscriptionUseCase.unsubscribe()
            }

            if (isSuccess){
                _uiState.update { it.copy(notificationEnabled = enabled) }
            }
        }
    }

    fun onThresholdEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(thresholdEnabled = enabled) }
    }

    fun onPhMinChanged(value: String) {
        _uiState.update { it.copy(phMin = value) }
    }

    fun onPhMaxChanged(value: String) {
        _uiState.update { it.copy(phMax = value) }
    }

    fun onTdsMinChanged(value: String) {
        _uiState.update { it.copy(tdsMin = value) }
    }

    fun onTdsMaxChanged(value: String) {
        _uiState.update { it.copy(tdsMax = value) }
    }

    fun onDurationChanged(value: String) {
        _uiState.update { it.copy(duration = value) }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, saveSuccess = false) }

            val currentSettings = ThresholdModel(
                phMin = _uiState.value.phMin,
                phMax = _uiState.value.phMax,
                tdsMin = _uiState.value.tdsMin,
                tdsMax = _uiState.value.tdsMax,
                thresholdEnabled = _uiState.value.thresholdEnabled
            )

            when (val resource = updateThresholdsUseCase(currentSettings)) {
                is Resource.Success -> {
                    originalSettings = resource.data!!
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            saveSuccess = true,
                            phMin = resource.data.phMin,
                            phMax = resource.data.phMax,
                            tdsMin = resource.data.tdsMin,
                            tdsMax = resource.data.tdsMax,
                            thresholdEnabled = resource.data.thresholdEnabled
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                }
                is Resource.Loading -> {  }
            }
        }
    }

    fun onCancelClicked() {
        originalSettings?.let { settings ->
            _uiState.update {
                it.copy(
                    thresholdEnabled = settings.thresholdEnabled,
                    phMin = settings.phMin,
                    phMax = settings.phMax,
                    tdsMin = settings.tdsMin,
                    tdsMax = settings.tdsMax,
                    errorMessage = null,
                    saveSuccess = false
                )
            }
        }
    }

    fun onSaveOtherClicked() {
        viewModelScope.launch {
            originalOtherSettings = OtherModel(
                duration = _uiState.value.duration,
            )

            manageSettingsDataUseCase.setDuration(_uiState.value.duration.toFloat())

            val duration = manageSettingsDataUseCase.getDuration().toString()

            _uiState.update {
                it.copy(
                    saveSuccess = true,
                    duration = duration
                )
            }
        }
    }

    fun onCancelOtherClicked() {
        originalOtherSettings?.let { settings ->
            _uiState.update {
                it.copy(
                    duration = settings.duration
                )
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun dismissSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}

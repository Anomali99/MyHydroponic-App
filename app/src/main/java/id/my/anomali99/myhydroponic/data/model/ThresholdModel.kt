package id.my.anomali99.myhydroponic.data.model

import androidx.annotation.Keep

@Keep
data class ThresholdModel(
    val phMin: String,
    val phMax: String,
    val tdsMin: String,
    val tdsMax: String,
    val thresholdEnabled: Boolean
)
package id.my.anomali99.myhydroponic.data.remote.api.dto

import com.google.gson.annotations.SerializedName

data class ThresholdDto(
    @SerializedName("ph_min")
    val phMin: Double,

    @SerializedName("ph_max")
    val phMax: Double,

    @SerializedName("tds_min")
    val tdsMin: Int,

    @SerializedName("tds_max")
    val tdsMax: Int,

    @SerializedName("is_active")
    val thresholdEnabled: Boolean,
)

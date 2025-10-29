package id.my.anomali99.myhydroponic.data.model

import com.google.gson.annotations.SerializedName

data class EnvironmentModel(
    @SerializedName("ph")
    val ph: Float,

    @SerializedName("tds")
    val tds: Float,

    @SerializedName("tank_main")
    val mainTank: Float,

    @SerializedName("tank_ph_up")
    val phUpTank: Float,

    @SerializedName("tank_ph_down")
    val phDownTank: Float,

    @SerializedName("tank_a")
    val aTank: Float,

    @SerializedName("tank_b")
    val bTank: Float,

    @SerializedName("datetime")
    val datetime: String,
)

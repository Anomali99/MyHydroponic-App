package id.my.anomali99.myhydroponic.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

import id.my.anomali99.myhydroponic.data.remote.api.dto.ThresholdDto

interface ApiService {

    @GET("threshold")
    suspend fun getThresholds(): Response<ThresholdDto>

    @PATCH("threshold/update")
    suspend fun updateThresholds(@Body thresholds: ThresholdDto): Response<ThresholdDto>
}

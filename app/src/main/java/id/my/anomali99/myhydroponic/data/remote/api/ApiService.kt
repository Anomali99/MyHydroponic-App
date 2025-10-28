package id.my.anomali99.myhydroponic.data.remote.api

import id.my.anomali99.myhydroponic.data.remote.api.dto.ThresholdDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ApiService {

    @GET("threshold")
    suspend fun getThresholds(): Response<ThresholdDto>

    @PATCH("threshold/update")
    suspend fun updateThresholds(@Body thresholds: ThresholdDto): Response<ThresholdDto>
}

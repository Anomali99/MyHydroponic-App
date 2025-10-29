package id.my.anomali99.myhydroponic.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

import id.my.anomali99.myhydroponic.data.remote.api.ApiService
import id.my.anomali99.myhydroponic.data.remote.api.dto.ThresholdDto
import id.my.anomali99.myhydroponic.data.model.ThresholdModel
import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository
import id.my.anomali99.myhydroponic.utils.Resource

class SettingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SettingsRepository {

    override fun getThresholds(): Flow<Resource<ThresholdModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getThresholds()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val domainModel = ThresholdModel(
                    phMin = dto.phMin.toString(),
                    phMax = dto.phMax.toString(),
                    tdsMin = dto.tdsMin.toString(),
                    tdsMax = dto.tdsMax.toString(),
                    thresholdEnabled = dto.thresholdEnabled
                )
                emit(Resource.Success(domainModel))
            } else {
                emit(Resource.Error("Gagal mengambil data: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Terjadi kesalahan HTTP: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Tidak dapat terhubung ke server. Periksa koneksi internet Anda."))
        }
    }

    override suspend fun updateThresholds(settings: ThresholdModel): Resource<ThresholdModel> {
        return try {
            val dto = ThresholdDto(
                phMin = settings.phMin.toDoubleOrNull() ?: 0.0,
                phMax = settings.phMax.toDoubleOrNull() ?: 0.0,
                tdsMin = settings.tdsMin.toIntOrNull() ?: 0,
                tdsMax = settings.tdsMax.toIntOrNull() ?: 0,
                thresholdEnabled = settings.thresholdEnabled
            )

            val response = apiService.updateThresholds(dto)

            if (response.isSuccessful && response.body() != null) {
                val resultDto = response.body()!!
                val resultDomainModel = ThresholdModel(
                    phMin = resultDto.phMin.toString(),
                    phMax = resultDto.phMax.toString(),
                    tdsMin = resultDto.tdsMin.toString(),
                    tdsMax = resultDto.tdsMax.toString(),
                    thresholdEnabled = resultDto.thresholdEnabled
                )
                Resource.Success(resultDomainModel)
            } else {
                Resource.Error("Gagal memperbarui data: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Terjadi kesalahan HTTP: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
        }
    }
}

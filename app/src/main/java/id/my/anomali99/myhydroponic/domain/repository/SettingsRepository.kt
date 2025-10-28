package id.my.anomali99.myhydroponic.domain.repository

import id.my.anomali99.myhydroponic.data.model.ThresholdModel
import id.my.anomali99.myhydroponic.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getThresholds(): Flow<Resource<ThresholdModel>>

    suspend fun updateThresholds(settings: ThresholdModel): Resource<ThresholdModel>
}
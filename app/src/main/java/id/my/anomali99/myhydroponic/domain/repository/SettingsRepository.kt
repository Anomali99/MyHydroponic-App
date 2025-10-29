package id.my.anomali99.myhydroponic.domain.repository

import kotlinx.coroutines.flow.Flow

import id.my.anomali99.myhydroponic.data.model.ThresholdModel
import id.my.anomali99.myhydroponic.utils.Resource

interface SettingsRepository {

    fun getThresholds(): Flow<Resource<ThresholdModel>>

    suspend fun updateThresholds(settings: ThresholdModel): Resource<ThresholdModel>
}
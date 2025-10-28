package id.my.anomali99.myhydroponic.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.my.anomali99.myhydroponic.data.remote.api.ApiService
import id.my.anomali99.myhydroponic.data.repository.SettingsRepositoryImpl
import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository
import id.my.anomali99.myhydroponic.domain.usecase.GetThresholdUseCase
import id.my.anomali99.myhydroponic.domain.usecase.UpdateThresholdUseCase
import id.my.anomali99.myhydroponic.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(apiService: ApiService): SettingsRepository {
        return SettingsRepositoryImpl(apiService)
    }

    // Use cases tidak perlu @Singleton jika mereka stateless
    @Provides
    fun provideGetThresholdsUseCase(repository: SettingsRepository): GetThresholdUseCase {
        return GetThresholdUseCase(repository)
    }

    @Provides
    fun provideUpdateThresholdsUseCase(repository: SettingsRepository): UpdateThresholdUseCase {
        return UpdateThresholdUseCase(repository)
    }
}

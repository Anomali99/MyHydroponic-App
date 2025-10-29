package id.my.anomali99.myhydroponic.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.my.anomali99.myhydroponic.data.local.datastore.SettingsDataStore
import id.my.anomali99.myhydroponic.data.remote.api.ApiService
import id.my.anomali99.myhydroponic.data.remote.fcm.FcmManager // <-- Tambahan
import id.my.anomali99.myhydroponic.data.repository.MainRepositoryImpl // <-- Tambahan
import id.my.anomali99.myhydroponic.data.repository.SettingsRepositoryImpl
import id.my.anomali99.myhydroponic.domain.repository.MainRepository // <-- Tambahan
import id.my.anomali99.myhydroponic.domain.repository.SettingsRepository
import id.my.anomali99.myhydroponic.domain.usecase.GetThresholdUseCase
import id.my.anomali99.myhydroponic.domain.usecase.ManageTopicSubscriptionUseCase // <-- Tambahan
import id.my.anomali99.myhydroponic.domain.usecase.UpdateThresholdUseCase
import id.my.anomali99.myhydroponic.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    companion object {

        @Provides
        @Singleton
        fun provideApiService(): ApiService {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
            return SettingsDataStore(context)
        }

        @Provides
        @Singleton
        fun provideFcmManager(): FcmManager {
            return FcmManager()
        }

        @Provides
        @Singleton
        fun provideSettingsRepository(apiService: ApiService): SettingsRepository {
            return SettingsRepositoryImpl(apiService)
        }

        @Provides
        @Singleton
        fun provideGetThresholdsUseCase(repository: SettingsRepository): GetThresholdUseCase {
            return GetThresholdUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideUpdateThresholdsUseCase(repository: SettingsRepository): UpdateThresholdUseCase {
            return UpdateThresholdUseCase(repository)
        }

        @Provides
        @Singleton
        fun provideManageTopicSubscriptionUseCase(repository: MainRepository): ManageTopicSubscriptionUseCase {
            return ManageTopicSubscriptionUseCase(repository)
        }

    }
}
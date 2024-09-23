package com.kg.stockpricesapp.di

import android.app.Application
import androidx.room.Room
import com.kg.stockpricesapp.data.local.StockDatabase
import com.kg.stockpricesapp.data.remote.StockApi
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.domain.useCases.GetCompanyInfoUseCase
import com.kg.stockpricesapp.domain.useCases.GetIntradayInfoUseCase
import com.kg.stockpricesapp.domain.useCases.GetListingsUseCase
import com.kg.stockpricesapp.domain.useCases.StockUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockDatabase(app : Application) : StockDatabase
    {
        return Room.databaseBuilder(
            app,
            StockDatabase::class.java,
            StockDatabase.name
        ).build()
    }

    @Provides
    @Singleton
    fun provideStockApi() : StockApi
    {
        return Retrofit.Builder().baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: StockRepository) : StockUseCases
    {
        return StockUseCases(
            getListings = GetListingsUseCase(repository),
            getIntradayInfo = GetIntradayInfoUseCase(repository),
            getCompanyInfo = GetCompanyInfoUseCase(repository)
        )
    }
}
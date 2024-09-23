package com.kg.stockpricesapp.di

import com.kg.stockpricesapp.data.csv.CSVParser
import com.kg.stockpricesapp.data.csv.IntradayInfoParser
import com.kg.stockpricesapp.data.csv.Parser
import com.kg.stockpricesapp.data.repository.StockRepositoryImpl
import com.kg.stockpricesapp.domain.model.CompanyListing
import com.kg.stockpricesapp.domain.model.IntradayInfo
import com.kg.stockpricesapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindParser(parser : Parser) : CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(parser : IntradayInfoParser) : CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository
}
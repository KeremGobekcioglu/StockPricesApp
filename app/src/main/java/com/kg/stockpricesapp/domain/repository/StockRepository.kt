package com.kg.stockpricesapp.domain.repository

import com.kg.stockpricesapp.domain.model.CompanyInfo
import com.kg.stockpricesapp.domain.model.CompanyListing
import com.kg.stockpricesapp.domain.model.IntradayInfo
import com.kg.stockpricesapp.util.Resource
import kotlinx.coroutines.flow.Flow


interface StockRepository {
    // getAll , getSpecific

    // Domain layer cannot reach the data layer so we should use models
    suspend fun getListings(
        fetchFromRemote : Boolean , nameOfCompany : String
    )
    : Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol : String
    ) : Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ) : Resource<CompanyInfo>
}
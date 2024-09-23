package com.kg.stockpricesapp.domain.useCases

import com.kg.stockpricesapp.domain.model.CompanyListing
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListingsUseCase @Inject constructor(
    private val repository : StockRepository
) {
    suspend operator fun invoke(
        fetchFromRemote : Boolean = false ,
        nameOfCompany : String = "") : Flow<Resource<List<CompanyListing>>>
    {
        return repository.getListings(fetchFromRemote , nameOfCompany)
    }
}
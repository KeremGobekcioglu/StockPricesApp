package com.kg.stockpricesapp.domain.useCases

import com.kg.stockpricesapp.domain.model.CompanyInfo
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.util.Resource
import javax.inject.Inject

class GetCompanyInfoUseCase @Inject constructor(
    private val repository : StockRepository
) {
    suspend operator fun invoke(symbol : String) : Resource<CompanyInfo>
    {
        return repository.getCompanyInfo(symbol)
    }
}
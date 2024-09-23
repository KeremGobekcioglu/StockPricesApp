package com.kg.stockpricesapp.domain.useCases

import com.kg.stockpricesapp.domain.model.IntradayInfo
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.util.Resource
import javax.inject.Inject

class GetIntradayInfoUseCase @Inject constructor(
    private val repository: StockRepository
){
    suspend operator fun invoke(symbol : String) : Resource<List<IntradayInfo>>
    {
        return repository.getIntradayInfo(symbol)
    }
}
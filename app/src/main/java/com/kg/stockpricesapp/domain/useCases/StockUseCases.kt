package com.kg.stockpricesapp.domain.useCases

data class StockUseCases(
    val getCompanyInfo : GetCompanyInfoUseCase,
    val getListings : GetListingsUseCase,
    val getIntradayInfo : GetIntradayInfoUseCase
)

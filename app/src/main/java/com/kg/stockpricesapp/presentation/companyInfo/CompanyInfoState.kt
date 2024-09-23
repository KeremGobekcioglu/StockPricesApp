package com.kg.stockpricesapp.presentation.companyInfo

import com.kg.stockpricesapp.domain.model.CompanyInfo
import com.kg.stockpricesapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null
)
package com.kg.stockpricesapp.presentation.companyList

import com.kg.stockpricesapp.domain.model.CompanyListing

data class CompanyListState(
    val companies : List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchName : String = "",
    val errorMessage  : String? = null
)

package com.kg.stockpricesapp.presentation.companyList

sealed class CompanyListEvents {
    data object Refresh : CompanyListEvents()
    data class onSearchNameChange(val searchName : String) : CompanyListEvents()
}
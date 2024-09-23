package com.kg.stockpricesapp.domain.model

import com.kg.stockpricesapp.data.local.CompanyListingEntity

data class CompanyListing(
    val name : String,
    val symbol : String,
    val exchange : String
)

// Mapper function to convert CompanyListingEntity to CompanyListing
fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity
{
    return CompanyListingEntity(
        symbol = this.symbol,
        name = this.name,
        exchange = this.exchange
    )
}
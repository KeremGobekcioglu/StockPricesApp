package com.kg.stockpricesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kg.stockpricesapp.domain.model.CompanyListing

@Entity
data class CompanyListingEntity(
    val symbol : String,
    val name : String,
    val exchange : String,
    @PrimaryKey val id : Int? = null
)

// Mapper function to convert CompanyListingEntity to CompanyListing
fun CompanyListingEntity.toCompanyListing() : CompanyListing
{
    return CompanyListing(
        symbol = this.symbol,
        name = this.name,
        exchange = this.exchange
    )
}

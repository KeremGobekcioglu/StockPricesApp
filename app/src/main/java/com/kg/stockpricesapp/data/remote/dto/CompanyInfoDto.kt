package com.kg.stockpricesapp.data.remote.dto

import com.kg.stockpricesapp.domain.model.CompanyInfo
import com.squareup.moshi.Json

data class CompanyInfoDto(
    @field:Json(name  = "Name") val name : String?,
    @field:Json(name  = "Description") val description : String?,
    @field:Json(name  = "Symbol") val symbol : String?,
    @field:Json(name  = "Country") val country : String?,
    @field:Json(name  = "Industry") val industry : String?
)

// If user makes more than five call in a minute , user wont get a respond
fun CompanyInfoDto.toCompanyInfo() : CompanyInfo
{
    return CompanyInfo(
        name = name ?: "",
        description = description ?: "",
        symbol = symbol ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}

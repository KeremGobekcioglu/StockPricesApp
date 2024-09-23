package com.kg.stockpricesapp.data.remote

import com.kg.stockpricesapp.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Query("apiKey") yapınca çalışmıyordu çözdüm.
 * BU HATAYI ÖĞREN BU NE AMK 1 SAAT SÜRDÜ MİNİMUM
 */
interface StockApi {

    @GET("/query?function=LISTING_STATUS")
    suspend fun getStocks(
        @Query("apikey") apiKey : String = APIKEY
    ) : ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol : String,
        @Query("apikey") apiKey : String = APIKEY
    ) : ResponseBody

    // It returns JSON
    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol : String,
        @Query("apikey") apiKey : String = APIKEY
    ) : CompanyInfoDto
    // This is the companion object that holds the APIKEY and BASE_URL
    // It is created to avoid hardcoding the values in the code
    companion object {
        const val APIKEY = "MALU2YSA113X9RNU"
        const val BASE_URL = "https://www.alphavantage.co"
    }
}
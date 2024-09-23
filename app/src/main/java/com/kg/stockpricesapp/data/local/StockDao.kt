package com.kg.stockpricesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    // i need insert , clear , search
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(companyListingEntities: List<CompanyListingEntity>)

    @Query("DELETE FROM companylistingentity")
    suspend fun clearListings()

    /**
     * Searches for company listings in the database where the company name contains the given
     * nameOfCompany string (case-insensitive) or where the symbol matches the given nameOfCompany
     * string (case-sensitive).
     * LOWER(:nameOfCompany) is the parameter you are searching for, converted to lowercase.
     * '% || LOWER(:nameOfCompany) || %' means that the name column can have any characters
     * before and after the nameOfCompany string.
     */
    @Query(
        """SELECT * FROM companylistingentity 
        WHERE LOWER(name) LIKE '%' || LOWER(:nameOfCompany) || '%' OR 
        UPPER(:nameOfCompany) == symbol"""
    )
    suspend fun search(nameOfCompany : String) : List<CompanyListingEntity>
}
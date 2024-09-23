package com.kg.stockpricesapp.data.csv

import com.kg.stockpricesapp.domain.model.CompanyListing
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class Parser @Inject constructor() : CSVParser<CompanyListing> {

    // Override the parse method from the CSVParser interface
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        // Initialize CSVReader with InputStreamReader to read the input stream
        val csvReader = CSVReader(InputStreamReader(stream))

        // Use withContext to switch to the IO dispatcher for reading the CSV file
        return withContext(Dispatchers.IO) {
            // Read all lines from the CSV, drop the header, and map each line to a CompanyListing object
            csvReader
                .readAll() // Read all lines from the CSV
                .drop(1) // Drop the header line
                .mapNotNull { line ->
                    // Extract fields from the CSV line
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)

                    // Map the CSV line to a CompanyListing object, return null if any field is missing
                    CompanyListing(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also {
                    // Close the CSVReader after processing
                    csvReader.close()
                }
        }
    }
}
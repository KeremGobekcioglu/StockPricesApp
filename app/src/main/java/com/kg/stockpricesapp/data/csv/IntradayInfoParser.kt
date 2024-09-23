package com.kg.stockpricesapp.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.kg.stockpricesapp.data.remote.dto.IntradayInfoDto
import com.kg.stockpricesapp.data.remote.dto.toIntradayInfo
import com.kg.stockpricesapp.domain.model.CompanyListing
import com.kg.stockpricesapp.domain.model.IntradayInfo
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {

    // Override the parse method from the CSVParser interface
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
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
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(timestamp,close.toDouble())
                    dto.toIntradayInfo()
                }
                // shows the values of yesterday
                .filter {
                    it.date.dayOfMonth == LocalDate.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    // Close the CSVReader after processing
                    csvReader.close()
                }
        }
    }
}
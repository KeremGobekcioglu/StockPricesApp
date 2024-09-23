package com.kg.stockpricesapp.data.remote.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.kg.stockpricesapp.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class IntradayInfoDto(
    val timestamp : String,
    val close : Double
)

@RequiresApi(Build.VERSION_CODES.O)
fun IntradayInfoDto.toIntradayInfo() : IntradayInfo
{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern , Locale.getDefault())
    val localeDateTime = LocalDateTime.parse(timestamp , formatter)
    return IntradayInfo(
        date = localeDateTime,
        close = close
    )
}
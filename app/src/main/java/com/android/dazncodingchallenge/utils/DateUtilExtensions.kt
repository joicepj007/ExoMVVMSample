@file:JvmName("DateUtilExtensions")

package com.android.dazncodingchallenge.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

const val PATTERN_SERVER_DATE = "dd-MM-yyyy HH:mm:ss"
const val PATTERN_SERVER_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val PATTERN_TIME_24H = "HH:mm"
const val PATTERN_START_WITH_DAY= "dd-MM-yyyy"


@RequiresApi(Build.VERSION_CODES.O)
fun dateTimeConversionCheck(stringDate: String?) : LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    return LocalDateTime.parse(stringDate, formatter)
}

fun convertDateString(inputPattern: String, outputPattern: String, stringDate: String): String? {
    val originalFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
    val targetFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
    val requiredFormat = originalFormat.parse(stringDate)
    return requiredFormat?.let { targetFormat.format(requiredFormat) }
}

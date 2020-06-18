package com.diana.test.meddrawer.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

fun String.toDate() : LocalDate? {
    if (this.isNullOrEmpty()) return null
    val date = LocalDate.parse(this, ofPattern("dd/MM/yyyy", Locale.ENGLISH))
    return date
}
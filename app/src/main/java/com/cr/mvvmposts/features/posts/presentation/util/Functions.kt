package com.cr.mvvmposts.features.posts.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toReadableDate(): String {
    val date = Date(this)
    val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    val formattedDate = sdf.format(date)

    // Add suffix to day (st, nd, rd, th)
    val day = formattedDate.substringBefore(" ").toInt()
    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return formattedDate.replaceFirst(Regex("\\d+"), "$day$suffix")
}
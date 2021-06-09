package org.anitab.mentorship.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "dd MMM yyyy"
const val SEND_REQUEST_END_DATE_FORMAT = "dd/MM/yyyy"
const val EXTENDED_DATE_FORMAT = "dd MMMM yyyy"
const val MILLISECONDS_FACTOR = 1000L

/**
 * Get [Date] from [unixTimestamp] in [String] format
 * @param unixTimestamp time in Unix timestamp format, in seconds
 * @param format date string format to use
 * @return date in string format
 */
fun convertUnixTimestampIntoStr(unixTimestamp: Float, format: String): String {

    val date = Date(getUnixTimestampInMilliseconds(unixTimestamp))
    val sdf = SimpleDateFormat(format)
    return sdf.format(date)
}

/**
 * Convert Unix timestamp seconds to milliseconds [Long] format
 * @param unixTimestamp time in Unix timestamp format, in seconds
 * @return unix timestamp in milliseconds
 */
fun getUnixTimestampInMilliseconds(unixTimestamp: Float): Long = (unixTimestamp * MILLISECONDS_FACTOR).toLong()

/**
 * Convert Date to UnixTimestamp [Long] format
 * @param dateStr date in string format
 * @param format string format used to parse dateStr
 * @return unix timestamp in milliseconds
 */
fun convertDateIntoUnixTimestamp(dateStr: String, format: String): Long {

    val date = SimpleDateFormat(format, Locale.US).parse(dateStr)
    return date.time / MILLISECONDS_FACTOR
}

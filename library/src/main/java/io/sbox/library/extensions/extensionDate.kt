package io.sbox.library.extensions

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*





//"Wed, 18 Apr 2012 07:55:29 +0000" // "EEE, dd MMM yyyy hh:mm:ss Z"
//"MMM dd,yyyy hh:mm a"
/*
public static final String DATE_FORMAT_1 = "HH:mm:ss";
public static final String DATE_FORMAT_2 = "h:mm a";
public static final String DATE_FORMAT_3 = "yyyy-MM-dd";
public static final String DATE_FORMAT_4 = "dd-MMMM-yyyy";
public static final String DATE_FORMAT_5 = "dd MMMM yyyy";
public static final String DATE_FORMAT_6 = "dd MMMM yyyy zzzz";
public static final String DATE_FORMAT_7 = "EEE, MMM d, ''yy";
public static final String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";
public static final String DATE_FORMAT_9 = "h:mm a dd MMMM yyyy";
public static final String DATE_FORMAT_10 = "K:mm a, z";
public static final String DATE_FORMAT_11 = "hh 'o''clock' a, zzzz";
public static final String DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
public static final String DATE_FORMAT_13 = "E, dd MMM yyyy HH:mm:ss z";
public static final String DATE_FORMAT_14 = "yyyy.MM.dd G 'at' HH:mm:ss z";
public static final String DATE_FORMAT_15 = "yyyyy.MMMMM.dd GGG hh:mm aaa";
public static final String DATE_FORMAT_16 = "EEE, d MMM yyyy HH:mm:ss Z";
public static final String DATE_FORMAT_17 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
public static final String DATE_FORMAT_18 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
public static final String DATE_FORMAT_19 = "dd-MMM-yyyy";
 */
enum class DatePattern(var format: String) {
    TypeDate01("yyyy-MM-dd"),

    TypeTime01("HH:mm:ss"),

    TypeFull01("yyyy-MM-dd HH:mm:ss")
}

fun String.getDate(dateFormat: DatePattern): Date {
    return getDate(dateFormat.format)
}
fun String.getDate(format: String): Date {
    return SimpleDateFormat(format).parse(this)
}


fun getCurrentDate(dateFormat: DatePattern = DatePattern.TypeDate01): String {
    return Calendar.getInstance().time.getFormatDate(dateFormat)
}
fun getCurrentDate(format: String): String {
    return Calendar.getInstance().time.getFormatDate(format)
}


fun Date.getFormatDate(dateFormat: DatePattern = DatePattern.TypeDate01): String {
    return getFormatDate(dateFormat.format)
}
fun Date.getFormatDate(format: String): String {
    return SimpleDateFormat(format).format(this)
}



fun Date.formatDateTime(locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, SimpleDateFormat.MEDIUM, locale).format(this)
}

val Date.formatDateTime: String
    get() = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, SimpleDateFormat.MEDIUM, Locale.US).format(this)

fun Date.formatMonthDate(locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat("MMM dd", locale).format(this)
}

fun Date.formatDate(locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat("MMM dd,yyyy", locale).format(this)
}

fun Long.compareCurrentMilliseconds(elapsedTime: Long = 60000L): Boolean {
    val referenceTime = this + elapsedTime
    val currentTime = System.currentTimeMillis()
    return currentTime < referenceTime
}
val Long.formatDate: String
    get() = Date(this).formatDate()

val Int.formatDays: String
    get() = String()


val Date.dDay: String
    get() {
        


        return ""
    }







/*
package io.skizo.library.extensions

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


//"Wed, 18 Apr 2012 07:55:29 +0000" // "EEE, dd MMM yyyy hh:mm:ss Z"
//"MMM dd,yyyy hh:mm a"
/*
public static final String DATE_FORMAT_1 = "HH:mm:ss";
public static final String DATE_FORMAT_2 = "h:mm a";
public static final String DATE_FORMAT_3 = "yyyy-MM-dd";
public static final String DATE_FORMAT_4 = "dd-MMMM-yyyy";
public static final String DATE_FORMAT_5 = "dd MMMM yyyy";
public static final String DATE_FORMAT_6 = "dd MMMM yyyy zzzz";
public static final String DATE_FORMAT_7 = "EEE, MMM d, ''yy";
public static final String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";
public static final String DATE_FORMAT_9 = "h:mm a dd MMMM yyyy";
public static final String DATE_FORMAT_10 = "K:mm a, z";
public static final String DATE_FORMAT_11 = "hh 'o''clock' a, zzzz";
public static final String DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
public static final String DATE_FORMAT_13 = "E, dd MMM yyyy HH:mm:ss z";
public static final String DATE_FORMAT_14 = "yyyy.MM.dd G 'at' HH:mm:ss z";
public static final String DATE_FORMAT_15 = "yyyyy.MMMMM.dd GGG hh:mm aaa";
public static final String DATE_FORMAT_16 = "EEE, d MMM yyyy HH:mm:ss Z";
public static final String DATE_FORMAT_17 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
public static final String DATE_FORMAT_18 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
public static final String DATE_FORMAT_19 = "dd-MMM-yyyy";
 */
enum class DateFormat(var format: String) {
    TypeDate01("yyyy-MM-dd"),

    TypeTime01("hh:mm:ss"),

    TypeFull01("yyyy-MM-dd HH:mm:ss")
}

fun String.getDate(dateFormat: DateFormat): Date {
    return getDate(dateFormat.format)
}
fun String.getDate(format: String): Date {
    return SimpleDateFormat(format).parse(this)
}


fun getCurrentDate(dateFormat: DateFormat = DateFormat.TypeDate01): String {
    return Calendar.getInstance().time.getFormatDate(dateFormat)
}
fun getCurrentDate(format: String): String {
    return Calendar.getInstance().time.getFormatDate(format)
}


fun Date.getFormatDate(dateFormat: DateFormat = DateFormat.TypeDate01): String {
    return getFormatDate(dateFormat.format)
}
fun Date.getFormatDate(format: String): String {
    return SimpleDateFormat(format).format(this)
}

 */
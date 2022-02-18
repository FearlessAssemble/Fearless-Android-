package io.sbox.library.extensions

import java.text.NumberFormat
import java.util.*


val Int.formatSupportCount: String
    get() = NumberFormat.getNumberInstance(Locale.US).format(this)

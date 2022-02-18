package io.sbox.library.extensions

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable


fun Bundle.put(vararg args: Pair<String, Any?>): Bundle = args.fold(this, { bundle, (key, value) ->
    when (value) {
        null -> bundle.putString(key, null)
        is Boolean -> bundle.putBoolean(key, value)
        is BooleanArray -> bundle.putBooleanArray(key, value)
        is Bundle -> bundle.putBundle(key, value)
        is Byte -> bundle.putByte(key, value)
        is ByteArray -> bundle.putByteArray(key, value)
        is String -> bundle.putString(key, value)
        is Char -> bundle.putChar(key, value)
        is CharArray -> bundle.putCharArray(key, value)
        is CharSequence -> bundle.putCharSequence(key, value)
        is Double -> bundle.putDouble(key, value)
        is DoubleArray -> bundle.putDoubleArray(key, value)
        is Float -> bundle.putFloat(key, value)
        is FloatArray -> bundle.putFloatArray(key, value)
        is Short -> bundle.putShort(key, value)
        is ShortArray -> bundle.putShortArray(key, value)
        is Int -> bundle.putInt(key, value)
        is IntArray -> bundle.putIntArray(key, value)
        is Long -> bundle.putLong(key, value)
        is LongArray -> bundle.putLongArray(key, value)
        is Parcelable -> bundle.putParcelable(key, value)
        is Serializable -> bundle.putSerializable(key, value)
        is Array<*> -> {
            if (value.size > 0 && value.all { it is String }) {
                bundle.putStringArray(key, value.map { it as String }.toTypedArray())
            } else if (value.size > 0 && value.all {it is CharSequence}) {
                bundle.putCharSequenceArray(key, value.map { it as CharSequence }.toTypedArray())
            } else if (value.size > 0 && value.all {it is Parcelable }) {
                bundle.putParcelableArray(key, value.map { it as Parcelable }.toTypedArray())
            } else {
                trace("$key の配列における型パラメータが Bundle で扱えない.")
            }
        }
        is ArrayList<*> -> {
            if (value.size > 0 && value.all { it is String }) {
                bundle.putStringArrayList(key, ArrayList(value.map { it as String }))
            } else if (value.size > 0 && value.all {it is CharSequence}) {
                bundle.putCharSequenceArrayList(key, ArrayList(value.map { it as CharSequence }))
            } else if (value.size > 0 && value.all {it is Int}) {
                bundle.putIntegerArrayList(key, ArrayList(value.map { it as Int }))
            } else if (value.size > 0 && value.all {it is Parcelable }) {
                bundle.putParcelableArrayList(key, ArrayList(value.map { it as Parcelable }))
            } else {
                trace("$key の ArrayList における型パラメータが Bundle で扱えない.")
            }
        }
        else -> trace(key + "に対応する値が解釈できない.")
    }
    bundle
})
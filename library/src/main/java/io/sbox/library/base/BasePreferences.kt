package io.sbox.library.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import io.sbox.library.extensions.dispatcherEvent


/*
class AppPreference: BasePreferences() {
    companion object {
        fun uuid() {
            base.set("uuid", UUID.randomUUID().toString())
        }
        var app: Boolean = true
            get() = base.get("app", true)
            set(value) {
                field = base.set("app", value)
            }
        }
     }
}
*/


open class BasePreferences {

    companion object {

        private var pref: SharedPreferences? = null
        val base: BasePreferences =
            BasePreferences()

        fun init(context: Context, preferences_name: String = "pref") {
            pref = context.getSharedPreferences(preferences_name, Context.MODE_PRIVATE)
            /*
            pref?.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                dispatcherEvent(key)
            }
            */
        }
    }

    fun clear() {
        pref?.edit()?.clear()?.apply()
    }

    fun isContains(key: String): Boolean {
        return pref?.contains(key)!!
    }

    fun <T> set(key: String, value: T): T {
        pref?.edit()?.apply {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }?.apply()
        return value
    }

    @SuppressLint("ApplySharedPref")
    fun <T> setCommit(key: String, value: T): T {
        pref?.edit()?.apply {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }?.commit()
        return value
    }

    @SuppressLint("ApplySharedPref")
    fun removeCommit(key: String) {
        pref?.edit()?.apply {
            remove(key)
        }?.apply()
    }

    fun <T:Any?> get(key: String, defalut: T): T {
        pref?.apply {
            return when (defalut) {
                is Int -> getInt(key, defalut)
                is String -> getString(key, defalut)
                is Float -> getFloat(key, defalut)
                is Long -> getLong(key, defalut)
                is Boolean -> getBoolean(key, defalut)
                else -> defalut
            } as T
        }
        return defalut
    }
}



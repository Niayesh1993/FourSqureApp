package com.example.routingapp.utility

import android.content.Context
import android.content.SharedPreferences


/**
 * Created by Zohre Niayeshi on 07,July,2020 niayesh1993@gmail.com
 **/
object SettingsManager {

    private var preferences: SharedPreferences? = null
    private val PREF_NAME = "NexFonPrefs"


    fun init(context: Context) {

        if (preferences == null)
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }


    fun setValue(key: String, value: String) {
        val editor = preferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setValue(key: String, value: Int) {
        val editor = preferences!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setValue(key: String, value: Boolean) {
        val editor = preferences!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setValue(key: String, value: Double) {
        val editor = preferences!!.edit()
        editor.putLong(key, java.lang.Double.doubleToLongBits(value))
        editor.apply()
    }

    fun getString(key: String): String? {
        return preferences!!.getString(key, "")
    }

    fun getInt(key: String): Int {
        return preferences!!.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return preferences!!.getBoolean(key, false)
    }

    fun getDouble(key: String): Double {
        return java.lang.Double.longBitsToDouble(preferences!!.getLong(key, 0))
    }

    fun hasValue(key: String): Boolean {
        return preferences!!.contains(key)
    }

    fun clearValue(key: String): Boolean {
        if (hasValue(key)) {
            preferences!!.edit().remove(key).apply()
            return true
        }
        return false
    }
}
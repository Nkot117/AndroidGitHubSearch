package com.example.androidgithubsearch.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences) {

    fun savePref(key: SharedPreferencesKeys, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key.value, value)
        editor.apply()
    }
    
    fun getPref(key: SharedPreferencesKeys): String? {
        return sharedPreferences.getString(key.value, "")
    }
}

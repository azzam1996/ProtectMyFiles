package com.azzam.protectmyfiles.data.sharedPref

import android.content.Context

class SharedPrefsImpl(context: Context) : SharedPrefs {

    companion object {
        private const val PREFERENCES_NAME = "protect_my_files_preferences"
        private const val PASSCODE = "passcode"
        private const val IV = "iv"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)


    override fun savePasscode(value: String) {
        preferences.edit().putString(PASSCODE, value).apply()
    }

    override fun getPasscode(): String? {
        return preferences.getString(PASSCODE, null)
    }

    override fun saveIV(value: String) {
        preferences.edit().putString(IV, value).apply()
    }

    override fun getIV(): String? {
        return preferences.getString(IV, null)
    }

}
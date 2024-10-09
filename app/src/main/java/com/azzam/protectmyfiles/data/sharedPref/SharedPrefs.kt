package com.azzam.protectmyfiles.data.sharedPref

interface SharedPrefs {
    fun savePasscode(value: String)
    fun getPasscode(): String?

    fun saveIV(value: String)
    fun getIV(): String?
}
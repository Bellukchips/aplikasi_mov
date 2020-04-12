package com.belluk.movapps.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUsers(val context:Context) {
    companion object{
        const val PREF_USER = "PREF_USER"

    }
    val sharePref = context.getSharedPreferences(PREF_USER,0)
    fun setValues(key:String,value:String){
        val editor:SharedPreferences.Editor = sharePref.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun getValues(key: String): String? {
        return sharePref.getString(key,"")
    }
}
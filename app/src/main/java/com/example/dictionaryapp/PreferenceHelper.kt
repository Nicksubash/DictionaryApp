package com.example.dictionaryapp

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val FILE_KEY="Subash Parajuli"
    private const val Welcome_Text="WELCOME"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)
    }

    fun setWelcomeText(context: Context, welcomeText:String){
        val sharedPreferences= getSharedPreferences(context)
        with(sharedPreferences.edit()){
            putString(FILE_KEY, Welcome_Text)
            apply()
        }
    }

    fun getWelcomeText(context: Context):String? {
        val sharedPreferences= getSharedPreferences(context)
        return sharedPreferences.getString(FILE_KEY,"Hello Dictionary")

    }
}
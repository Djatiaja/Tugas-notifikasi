package com.example.tugasnotifikasi.SharedPreference

import android.content.Context
import android.content.SharedPreferences

class PrefManager  private constructor(context: Context){
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    companion object{
        private const val FILE_NAME ="SharedPreferencesApp"
        @Volatile
        private var instance:PrefManager?=null

        fun getInstance(context: Context):PrefManager{
            return instance?: synchronized(this){
                instance?:PrefManager(context.applicationContext).also {
                    instance= it
                }
            }
        }

        private const val KEY_USERNAME = "username"
    }

    fun saveUsername(username:String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }

    fun clear(){
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }

}
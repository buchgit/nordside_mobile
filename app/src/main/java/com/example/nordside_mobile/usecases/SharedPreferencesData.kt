package com.example.nordside_mobile.usecases

import android.content.Context
import android.content.SharedPreferences
import com.example.nordside_mobile.MyApp

//TODO переписать class as singleton
class SharedPreferencesData{

    var token: String? = null

    companion object{
        var appSettins: SharedPreferences? = MyApp.getContext()?.getSharedPreferences(
            ApplicationConstants().SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE
        )
    }

    init {
        token = appSettins?.getString(ApplicationConstants().TOKEN, "")
    }
}
package com.example.nordside_mobile.data.preferences

import android.content.Context
import com.example.nordside_mobile.utils.ApplicationConstants

private const val KEY = "key"


class PreferenceProvider(
    context : Context
) {

    private val appContext = context.applicationContext

    private val appPreference = appContext.getSharedPreferences(
        ApplicationConstants().SHARED_PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )

    fun saveString(key: String, value: String){
        appPreference.edit()
            .putString(key, value)
            .apply()
    }

    fun getSavedString(key: String) : String?{
        return appPreference.getString(key, null)
    }


}
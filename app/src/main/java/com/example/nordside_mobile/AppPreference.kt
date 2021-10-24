package com.example.nordside_mobile

import android.content.Context
import com.example.nordside_mobile.usecases.ApplicationConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreference @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val appPreference = appContext.getSharedPreferences(
        ApplicationConstants().SHARED_PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )

    fun saveString(key: String, value: String?) {
        appPreference.edit()
            .putString(key, value)
            .apply()
    }

    fun getSavedString(key: String): String? {
        return appPreference.getString(key, null)
    }


}
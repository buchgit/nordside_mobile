package com.example.nordside_mobile.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.utils.ApplicationConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val TAG = "GET_TOKEN_USECASE"

class GetTokenUseCase {

    suspend fun execute(
        loginBody: LoginBody,
        repositoryApi: NordsideRepository,
        context: Context
    ) : LiveData<ServerToken>? {
        var tokenLiveData: LiveData<ServerToken>?= null
        try {

            withContext(Dispatchers.Default) {
                tokenLiveData = repositoryApi.login(loginBody)
            }

            // Можно вынести в отдельный класс PreferenceProvider
            val appSettings = context.applicationContext.getSharedPreferences(
                ApplicationConstants().SHARED_PREFERENCES_FILE,
                Context.MODE_PRIVATE
            )
            appSettings.edit()
                .putString(ApplicationConstants().TOKEN, tokenLiveData?.value?.token)
                .apply()
            //

        } catch (throwable: Throwable) {
            when(throwable) {
                is HttpException -> {
                    Log.v(TAG, "HttpException")
                }
                else -> {
                    Log.v(TAG, "API exception")
                }
            }
        }

        // Лучше всего обернуть возвращаемый токен в Resource
        return tokenLiveData
    }

    companion object {
        fun newInstance() = GetTokenUseCase()
    }
}
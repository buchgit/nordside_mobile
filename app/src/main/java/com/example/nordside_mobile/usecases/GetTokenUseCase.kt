package com.example.nordside_mobile.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.utils.ApplicationConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "GET_TOKEN_USECASE"

class GetTokenUseCase @Inject constructor(
    private val repositoryApi: NordsideRepository,
    private val sharedPreferences: AppPreference
) {

    suspend fun execute(
        loginBody: LoginBody,
    ) : LiveData<ServerToken>? {
        var tokenLiveData: LiveData<ServerToken>?= null
        try {

            withContext(Dispatchers.Default) {
                tokenLiveData = repositoryApi.login(loginBody)
            }

            sharedPreferences.saveString(ApplicationConstants().TOKEN, tokenLiveData?.value?.token)

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

}
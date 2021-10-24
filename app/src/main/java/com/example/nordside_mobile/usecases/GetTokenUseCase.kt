package com.example.nordside_mobile.usecases

import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository

import com.example.nordside_mobile.repository.Resource

import javax.inject.Inject

private const val TAG = "GET_TOKEN_USECASE"

class GetTokenUseCase @Inject constructor(
    private val repositoryApi: NordsideRepository,
    private val sharedPreferences: AppPreference
) {

    suspend fun execute(
        loginBody: LoginBody,

    ) : Resource<ServerToken> {
        val tokenResource = repositoryApi.login(loginBody)

        if (tokenResource is Resource.Success) {
            sharedPreferences.saveString(
                ApplicationConstants().TOKEN,
                tokenResource.data?.token
            )
        }

        return tokenResource
    }
}
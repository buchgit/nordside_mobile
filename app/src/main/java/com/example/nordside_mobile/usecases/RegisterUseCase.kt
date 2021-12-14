package com.example.nordside_mobile.usecases

import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repositoryApi: NordsideRepository,
) {
    suspend fun execute(loginBody: LoginBody): Resource<Boolean> {
        val registerStatusResource = repositoryApi.register(loginBody)
        return registerStatusResource
    }
}

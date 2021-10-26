package com.example.nordside_mobile.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.impl.TextCodec
import io.jsonwebtoken.lang.Strings
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repositoryApi: NordsideRepository,
    private val sharedPreferences: AppPreference
) {

    val TAG = "${GetTokenUseCase::class.java.simpleName} ###"

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun execute(
        loginBody: LoginBody,

    ) : Resource<ServerToken> {
        val tokenResource = repositoryApi.login(loginBody)

        if (tokenResource is Resource.Success) {
            sharedPreferences.saveString(ApplicationConstants().ACCESS_TOKEN, tokenResource.data?.accessToken)
            sharedPreferences.saveString(ApplicationConstants().REFRESH_TOKEN, tokenResource.data?.refreshToken)
        //TODO  (for debag) , delete later
        //val expired : Boolean = isTokenExpired(tokenResource.data?.token)
        //Log.v(TAG,"tokenResource expired is $expired" )
        }

        return tokenResource
    }

}
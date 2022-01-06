package com.example.nordside_mobile.usecases

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.impl.TextCodec
import io.jsonwebtoken.lang.Strings
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repositoryApi: NordsideRepository,
    private val sharedPreferences: AppPreference
)  {

    val TAG = "${GetTokenUseCase::class.java.simpleName} ###"

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun execute(loginBody: LoginBody?): Resource<ServerToken> {

        val tokenResource: Resource<ServerToken> = if (loginBody == null) {
            val token: String? = sharedPreferences.getSavedString(ApplicationConstants().REFRESH_TOKEN)
            if (token != null) {
                 repositoryApi.refreshToken(token)
            }
           Resource.Error("refresh token error")
        } else {
            repositoryApi.login(loginBody)
        }

        if (tokenResource is Resource.Success) {
            sharedPreferences.saveString(
                ApplicationConstants().ACCESS_TOKEN,
                tokenResource.data?.accessToken
            )
            Log.v(TAG, "saved access token ${tokenResource.data?.accessToken}")
            sharedPreferences.saveString(
                ApplicationConstants().REFRESH_TOKEN,
                tokenResource.data?.refreshToken
            )
            Log.v(TAG, "saved refresh token ${tokenResource.data?.refreshToken}")
        } else {
            Log.v(TAG, "tokenResource is not Resource.Success ${tokenResource.message}")
        }
        return tokenResource
    }
}

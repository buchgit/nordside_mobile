package com.example.nordside_mobile.usecases

import android.os.Build
import android.util.Log
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
            sharedPreferences.saveString(ApplicationConstants().TOKEN, tokenResource.data?.token)
        //TODO  (for debag) , delete later
        //val expired : Boolean = isTokenExpired(tokenResource.data?.token)
        //Log.v(TAG,"tokenResource expired is $expired" )
        }

        return tokenResource
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isTokenExpired(tokenString: String?): Boolean {

        if (tokenString == null) {
            return false
        }

        var delimiterCount = 0

        val sb = StringBuilder(128)

        var expireDate: LocalDate = LocalDate.now()

        //var base64UrlEncodedHeader:String = ""
        var base64UrlEncodedPayload:String = ""

        for (c in tokenString.toCharArray()) {
            if (c == JwtParser.SEPARATOR_CHAR) {
                val tokenSeq = Strings.clean(sb)
                val token = tokenSeq?.toString()
                if (delimiterCount == 0) {
                    //base64UrlEncodedHeader = token!!
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token!!
                }
                delimiterCount++
                sb.setLength(0)
            } else {
                sb.append(c)
            }
        }

        val payload:String = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload)
        if (payload[0] == '{' && payload[payload.length - 1] == '}') { //likely to be json, parse it:
            val parseString = payload.substringAfter("exp\":").substringBefore("}")

            expireDate = Instant.ofEpochSecond(parseString.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()

        }
        return expireDate.isBefore(LocalDate.now(ZoneId.systemDefault()))
    }

}
package com.example.nordside_mobile.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nordside_mobile.AppPreference
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.impl.TextCodec
import io.jsonwebtoken.lang.Strings
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.properties.Delegates

@RequiresApi(Build.VERSION_CODES.O)
class AccessTokenUseCase(accessToken:String) {

    var token: String = ""
    var isExpared by Delegates.notNull<Boolean>()

    init {
        token = accessToken
        isExpared = isTokenExpired(accessToken)
    }

    private fun isTokenExpired(tokenString: String?): Boolean {

        if (tokenString == null) {
            return false
        }

        var delimiterCount = 0

        val sb = StringBuilder(128)

        var expireDate: LocalDate = LocalDate.now()

        //var base64UrlEncodedHeader:String = ""
        var base64UrlEncodedPayload: String = ""

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

        val payload: String = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload)
        if (payload[0] == '{' && payload[payload.length - 1] == '}') { //likely to be json, parse it:
            val parseString = payload.substringAfter("exp\":").substringBefore("}")

            expireDate = Instant.ofEpochSecond(parseString.toLong()).atZone(ZoneId.systemDefault())
                .toLocalDate()

        }
        return expireDate.isBefore(LocalDate.now(ZoneId.systemDefault()))
    }

}
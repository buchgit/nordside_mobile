package com.example.nordside_mobile.usecases

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class DbTypeConverter {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toURI(uri: String?): Uri? {
        return toURI(uri)
    }

    @TypeConverter
    fun fromURI(uri: Uri?): String? {
        return uri?.toString()
    }
}
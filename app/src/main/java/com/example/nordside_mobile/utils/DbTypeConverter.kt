package com.example.nordside_mobile.utils

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

}
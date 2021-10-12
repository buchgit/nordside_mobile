package com.example.nordside_mobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.entity.CartPosition
import com.example.nordside_mobile.utils.DbTypeConverter

@Database(entities = [CartPosition::class],version = 1)
@TypeConverters(DbTypeConverter::class)
abstract class NordsideDataBase : RoomDatabase() {

    abstract fun cartDao():CartDao

}
package com.example.nordside_mobile.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nordside_mobile.data.db.dao.CartDao
import com.example.nordside_mobile.data.db.entity.CartPosition
import com.example.nordside_mobile.data.db.typeconverters.DbTypeConverter

@Database(entities = [CartPosition::class],version = 1)
@TypeConverters(DbTypeConverter::class)
abstract class NordsideDataBase : RoomDatabase() {

    abstract fun cartDao():CartDao

}
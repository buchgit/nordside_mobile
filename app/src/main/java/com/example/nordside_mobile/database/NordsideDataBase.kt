package com.example.nordside_mobile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nordside_mobile.entity.CartPosition

@Database(entities = [CartPosition::class],version = 1)
abstract class NordsideDataBase : RoomDatabase() {



}
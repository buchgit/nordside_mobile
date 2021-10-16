package com.example.nordside_mobile.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CartPosition (@PrimaryKey val id:UUID = UUID.randomUUID(),
                         @ColumnInfo(name = "code") //при совпадении можно не писать
                         var code: String,
                         @ColumnInfo(name = "count")
                         var count:Double)

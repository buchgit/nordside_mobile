package com.example.nordside_mobile.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nordside_mobile.model.Nomenclature
import java.util.*

@Entity
data class CartPosition (@PrimaryKey val id:UUID = UUID.randomUUID(),
                         @ColumnInfo(name = "code") //при совпадении можно не писать
                         var code: String,
                         @ColumnInfo(name = "count")
                         var count:Int)

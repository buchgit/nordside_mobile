package com.example.nordside_mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nordside_mobile.entity.CartPosition

@Dao
interface CartDao {

    @Query("select * from CartPosition")
    fun getAllCartPositions():LiveData<List<CartPosition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCartPosition(cartPosition: CartPosition)

    @Query("update CartPosition set count = :count where code =:code")
    fun updateCartPosition(code: String, count:Int)

    @Query("delete from CartPosition where code=:code ")
    fun deleteCartPosition(code: String)

}
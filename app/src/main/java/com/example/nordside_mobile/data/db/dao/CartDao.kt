package com.example.nordside_mobile.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nordside_mobile.data.db.entity.CartPosition

@Dao
interface CartDao {

    @Query("select * from CartPosition")
    suspend fun getAllCartPositions():List<CartPosition>

    @Query("select sum(count) as total from CartPosition where code=:code ")
    fun getCartPositionsCount(code: String):LiveData<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCartPosition(cartPosition: CartPosition)

    @Query("update CartPosition set count = :count where code =:code")
    suspend fun updateCartPosition(code: String, count:Double)

    @Query("delete from CartPosition where code=:code ")
    suspend fun deleteCartPosition(code: String)

}
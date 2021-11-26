package com.example.nordside_mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.entity.CartPosition
import org.jetbrains.annotations.NotNull

@Dao
interface CartDao {

    @Query("select code, max(title) as title, max(unit) as unit, sum(count) as count, sum(summa) as summa from CartPosition group By code")
    fun getAllCartPositions(): LiveData<List<CartPositionPojo?>>

    @Query("select code, title, unit, sum(count) as count, sum(summa) as summa from CartPosition where code=:code ")
    //@Query("select s.* FROM(select code, title, unit, sum(count) as count, sum(summa) as summa from CartPosition where code=:code) as s where s.summa>0 ")
    fun getCartPositionsCount(@NotNull code: String):LiveData<CartPositionPojo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCartPosition(cartPosition: CartPosition)

    @Query("update CartPosition set count = :count, summa =:summa, title= :title , unit=:unit where code =:code")
    suspend fun updateCartPosition(code: String, count:Double, summa:Double, title:String, unit:String)

    @Query("delete from CartPosition where code=:code ")
    suspend fun deleteCartPosition(code: String)

    @Query("select sum(summa) as summa from CartPosition")
    fun getTotalCartSumma():LiveData<Double>

//    @Delete
//    suspend fun deleteAll()

}
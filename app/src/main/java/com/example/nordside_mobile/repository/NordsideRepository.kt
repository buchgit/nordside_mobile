package com.example.nordside_mobile.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.example.nordside_mobile.api.NordsideApi
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.database.SummaCountPojo
import com.example.nordside_mobile.entity.CartPosition
import com.example.nordside_mobile.model.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NordsideRepository @Inject constructor(
    private val cartDao: CartDao,
    private val nordsideApi: NordsideApi
) : BaseApiRepository() {

    private val TAG = "${NordsideRepository::class.java.simpleName} ###"

    suspend fun getNomenclatureList(): Resource<List<NomenclatureCollection>> {
        return safeApiCall { nordsideApi.getNomenclatureList() }
    }

    suspend fun getAllCategory(): Resource<List<Category>> {
        return safeApiCall { nordsideApi.getAllCategory() }
    }

    suspend fun getCollectionByCategoryId(id: String): Resource<List<NomenclatureCollection>> {
        return safeApiCall { nordsideApi.getCollectionByCategory(id) }
    }

    suspend fun getNomenclatureByCollection(id: String): Resource<List<Nomenclature>> {
        return safeApiCall { nordsideApi.getNomenclatureByCollection(id) }
    }

    suspend fun getAllPartner(): Resource<List<Partner>> {
        return safeApiCall { nordsideApi.getAllPartner() }
    }

    suspend fun login(login: LoginBody): Resource<ServerToken> {
        return safeApiCall { nordsideApi.login(login) }
    }


    @Transaction
    fun saveToCart(code:String, count:Double, summa:Double) = runBlocking{
        launch{
            cartDao.saveCartPosition(CartPosition(UUID.randomUUID(), code,count,summa))
            Log.v(TAG,cartDao.getCartPositionsCount(code).toString())
        }
    }

    fun updateCartPosition(code:String, count:Double, summa:Double) = runBlocking{
        launch{
            cartDao.updateCartPosition(code,count,summa)
        }
    }

    fun deleteCartPosition(code:String) = runBlocking{
        launch{
            cartDao.deleteCartPosition(code)
        }
    }

    fun getCartPositionsCount(code:String):LiveData<SummaCountPojo> {
        return cartDao.getCartPositionsCount(code)
    }
}




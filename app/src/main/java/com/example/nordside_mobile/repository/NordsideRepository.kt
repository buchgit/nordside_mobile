package com.example.nordside_mobile.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.Transaction
import com.example.nordside_mobile.api.NordsideApi
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.database.NordsideDataBase
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.entity.CartPosition
import com.example.nordside_mobile.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class NordsideRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val cartDao: CartDao,
    private val nordsideApi: NordsideApi
) : BaseApiRepository() {

    private val TAG = "${NordsideRepository::class.java.simpleName} ###"

    private val DATABASE_NAME = "nordside database"

    private val database: NordsideDataBase =
        Room.databaseBuilder(
            context.applicationContext,
            NordsideDataBase::class.java,
            DATABASE_NAME
        ).build()

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
        Log.v(TAG, "login() ->  ${login.email} #### ${login.password}")
        return safeApiCall { nordsideApi.login(login) }
    }

    suspend fun refreshToken(): Resource<ServerToken> {
        return safeApiCall { nordsideApi.refreshToken() }
    }

    suspend fun getPersonalNomenclatureListByCollection(id: String): Resource<List<PriceTable>> {
        return safeApiCall { nordsideApi.getPersonalNomenclatureListByCollection(id) }
    }

    @Transaction
    fun saveToCart(code: String, count: Double, summa: Double, title: String, unit: String) =
        runBlocking {
            launch {
                deleteCartPosition(code)
                cartDao.saveCartPosition(
                    CartPosition(
                        UUID.randomUUID(),
                        code,
                        count,
                        summa,
                        title,
                        unit
                    )
                )
                Log.v(TAG, cartDao.getCartPositionsCount(code).toString())
            }
        }

    fun updateCartPosition(
        code: String,
        count: Double,
        summa: Double,
        title: String,
        unit: String
    ) = runBlocking {
        launch {
            cartDao.updateCartPosition(code, count, summa, title, unit)
        }
    }

    fun deleteCartPosition(code: String) = runBlocking {
        launch {
            cartDao.deleteCartPosition(code)
        }
    }

    fun getCartPositionsCount(code: String): LiveData<CartPositionPojo?> {
        return cartDao.getCartPositionsCount(code)
    }

    fun getAllCartPosition(): LiveData<List<CartPositionPojo?>> {
        return cartDao.getAllCartPositions()
    }

}




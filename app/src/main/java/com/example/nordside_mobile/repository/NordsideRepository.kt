package com.example.nordside_mobile.repository

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    suspend fun refreshToken(token:String): Resource<ServerToken> {
        return safeApiCall { nordsideApi.refreshToken(token) }
    }

    suspend fun getPersonalNomenclatureListByCollection(token:String, id: String): Resource<List<PriceTable>> {
        return safeApiCall { nordsideApi.getPersonalNomenclatureListByCollection(token, id) }
    }

    suspend fun saveOrderOnServer(token: String, order: Order):Resource<String>{
        return safeApiCall { nordsideApi.saveOrderOnServer(token, order) }
    }

    suspend fun getPersonalOrderList(token: String):Resource<List<Order>>{
        return safeApiCall { nordsideApi.getPersonalOrderList(token) }
    }

    suspend fun register(loginBody: LoginBody) : Resource<Boolean> {
        return safeApiCall { nordsideApi.register(loginBody) }
    }

    @Transaction
    fun saveToCart(
        code: String, count: Double, summa: Double, title: String, unit: String, imageUri: Uri?
    ) =
        runBlocking {
            launch {
                deleteCartPosition(code)
                cartDao.saveCartPosition(
                    CartPosition(UUID.randomUUID(), code, count, summa, title, unit, imageUri)
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

    fun cleanCart() = runBlocking {
        launch {
            cartDao.delete()
        }
    }

    fun getCartPositionsCount(code: String): LiveData<CartPositionPojo?> {
        return cartDao.getCartPositionsCount(code)
    }

    fun getAllCartPosition(): Flow<List<CartPositionPojo?>?> {
        return cartDao.getAllCartPositions()
    }

    fun getTotalCartSumma(): Flow<Double?> {
        return cartDao.getTotalCartSumma()
    }

}
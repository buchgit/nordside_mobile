package com.example.nordside_mobile.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.Transaction
import com.example.nordside_mobile.utils.ApplicationConstants
import com.example.nordside_mobile.BuildConfig
import com.example.nordside_mobile.MyApp
import com.example.nordside_mobile.api.NordsideApi
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.database.NordsideDataBase
import com.example.nordside_mobile.database.SummaCountPojo
import com.example.nordside_mobile.entity.CartPosition
import com.example.nordside_mobile.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NordsideRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val cartDao: CartDao,
    private val nordsideApi: NordsideApi
) {

    private val TAG = "${NordsideRepository::class.java.simpleName} ###"


    fun getNomenclatureList(): LiveData<List<NomenclatureCollection>> {
        val nomenclatureCollectionList: MutableLiveData<List<NomenclatureCollection>> =
            MutableLiveData()
        val siteRequest: Call<List<NomenclatureCollection>> = nordsideApi.getNomenclatureList()
        siteRequest.enqueue(object : Callback<List<NomenclatureCollection>> {
            override fun onResponse(
                call: Call<List<NomenclatureCollection>>,
                response: Response<List<NomenclatureCollection>>
            ) {
                val responseBody: List<NomenclatureCollection>? = response.body()
                Log.v(TAG, "${responseBody?.size.toString()} -> onResponse")
                nomenclatureCollectionList.value = responseBody
            }

            override fun onFailure(call: Call<List<NomenclatureCollection>>, t: Throwable) {
                Log.v(TAG, "${t.stackTrace.toString()} ->  onFailure")
            }

        })
        return nomenclatureCollectionList
    }


    fun getAllCategory(): LiveData<List<Category>> {
        val listLiveData: MutableLiveData<List<Category>> = MutableLiveData()
        val siteRequest: Call<List<Category>> = nordsideApi.getAllCategory()
        siteRequest.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                val responseBody: List<Category>? = response.body()
                Log.v(TAG, "${responseBody?.size.toString()} -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.v(TAG, "${t.stackTrace.toString()} ->  onFailure")
            }
        })
        return listLiveData
    }


    fun getCollectionByCategoryId(id: String): LiveData<List<NomenclatureCollection>> {
        val listLiveData: MutableLiveData<List<NomenclatureCollection>> = MutableLiveData()
        val siteRequest: Call<List<NomenclatureCollection>> =
            nordsideApi.getCollectionByCategory(id)
        siteRequest.enqueue(object : Callback<List<NomenclatureCollection>> {
            override fun onResponse(
                call: Call<List<NomenclatureCollection>>,
                response: Response<List<NomenclatureCollection>>
            ) {
                val responseBody: List<NomenclatureCollection>? = response.body()
                Log.v(TAG, "${responseBody?.size.toString()} -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<List<NomenclatureCollection>>, t: Throwable) {

                Log.v(TAG, " getCollectionByCategoryId ->  onFailure")

            }
        })
        return listLiveData
    }


    fun getNomenclatureByCollection(id: String): LiveData<List<Nomenclature>> {
        val listLiveData: MutableLiveData<List<Nomenclature>> = MutableLiveData()
        val siteRequest: Call<List<Nomenclature>> = nordsideApi.getNomenclatureByCollection(id)
        siteRequest.enqueue(object : Callback<List<Nomenclature>> {
            override fun onResponse(
                call: Call<List<Nomenclature>>,
                response: Response<List<Nomenclature>>
            ) {
                val responseBody: List<Nomenclature>? = response.body()
                Log.v(TAG, "${responseBody?.size.toString()} -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<List<Nomenclature>>, t: Throwable) {
                Log.v(TAG, "${t.stackTrace.toString()} ->  onFailure")
            }
        })
        return listLiveData
    }


    fun getAllPartner(): LiveData<List<Partner>> {
        val listLiveData: MutableLiveData<List<Partner>> = MutableLiveData()
        val siteRequest: Call<List<Partner>> = nordsideApi.getAllPartner()
        siteRequest.enqueue(object : Callback<List<Partner>> {
            override fun onResponse(
                call: Call<List<Partner>>,
                response: Response<List<Partner>>
            ) {
                val responseBody: List<Partner>? = response.body()
                Log.v(TAG, "${responseBody?.size.toString()} -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<List<Partner>>, t: Throwable) {

                Log.v(TAG, "${t.message} getAllPartner() ->  onFailure")

            }
        })
        return listLiveData
    }


    fun login(login: LoginBody): LiveData<ServerToken> {
        val listLiveData: MutableLiveData<ServerToken> = MutableLiveData()
        val siteRequest: Call<ServerToken> = nordsideApi.login(login)
        siteRequest.enqueue(object : Callback<ServerToken> {
            override fun onResponse(
                call: Call<ServerToken>,
                response: Response<ServerToken>
            ) {
                val responseBody: ServerToken? = response.body()
                Log.v(TAG, "login -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<ServerToken>, t: Throwable) {
                Log.v(TAG, "login ->  onFailure")
            }
        })
        return listLiveData
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




package com.nordside_trading.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nordside_trading.ApplicationConstants
//import com.nordside_trading.ApplicationConstants
import com.nordside_trading.BuildConfig
import com.nordside_trading.MyApp
//import com.nordside_trading.MyApp
import com.nordside_trading.api.NordsideApi
import com.nordside_trading.model.*
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NordsideRepository() {

    val TAG = NordsideRepository::class.java.simpleName
    var nordsideApi: NordsideApi

    var appSettins:SharedPreferences? = MyApp.getContext()?.getSharedPreferences(
        ApplicationConstants().SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    var token: String? = appSettins?.getString(ApplicationConstants().TOKEN,"")

    init {


//        val client = OkHttpClient.Builder().addInterceptor { chain ->
//            val newRequest: Request = chain.request().newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .build()
//            chain.proceed(newRequest)
//        }
//            .build()

//        val client = OkHttpClient.Builder().addInterceptor { chain ->
//            val newRequest: Request = chain.request().newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .build()
//            chain.proceed(newRequest)
//        }
//            .build()


//        val client = OkHttpClient.Builder()
//            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS) )
//            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            //.client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nordsideApi = retrofit.create(NordsideApi::class.java)
    }

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

    fun getAllCategory():LiveData<List<Category>>{
        val listLiveData:MutableLiveData<List<Category>> = MutableLiveData()
        val siteRequest:Call<List<Category>> = nordsideApi.getAllCategory()
        siteRequest.enqueue(object :Callback<List<Category>>{
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

    fun getCollectionByCategoryId(id:String):LiveData<List<NomenclatureCollection>>{
        val listLiveData:MutableLiveData<List<NomenclatureCollection>> = MutableLiveData()
        val siteRequest:Call<List<NomenclatureCollection>> = nordsideApi.getCollectionByCategory(id)
        siteRequest.enqueue(object :Callback<List<NomenclatureCollection>>{
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

    fun getNomenclatureByCollection(id:String):LiveData<List<Nomenclature>>{
        val listLiveData:MutableLiveData<List<Nomenclature>> = MutableLiveData()
        val siteRequest:Call<List<Nomenclature>> = nordsideApi.getNomenclatureByCollection(id)
        siteRequest.enqueue(object :Callback<List<Nomenclature>>{
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

    fun getAllPartner():LiveData<List<Partner>>{
        val listLiveData:MutableLiveData<List<Partner>> = MutableLiveData()
        val siteRequest:Call<List<Partner>> = nordsideApi.getAllPartner()
        siteRequest.enqueue(object :Callback<List<Partner>>{
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


    fun login(login:LoginBody):LiveData<ServerToken>{
        val listLiveData:MutableLiveData<ServerToken> = MutableLiveData()
        val siteRequest:Call<ServerToken> = nordsideApi.login(login)
        siteRequest.enqueue(object :Callback<ServerToken>{
            override fun onResponse(
                call: Call<ServerToken>,
                response: Response<ServerToken>
            ) {
                val responseBody: ServerToken? = response.body()
                Log.v(TAG, "${responseBody?.token} -> onResponse")
                listLiveData.value = responseBody
            }

            override fun onFailure(call: Call<ServerToken>, t: Throwable) {
                Log.v(TAG, "${t.stackTrace.toString()} ->  onFailure")
            }
        })

        return listLiveData
    }

}
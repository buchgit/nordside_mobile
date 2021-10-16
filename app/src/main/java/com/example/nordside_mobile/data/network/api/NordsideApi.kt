package com.example.nordside_mobile.data.network.api

import com.example.nordside_mobile.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NordsideApi {
    @GET("rest/user/nomenclature/all?email=user@gmail.com")
    fun getAllNomenclature(): Call<List<PriceTable>>

    @GET("rest/user/nomenclature/all?email=user@gmail.com")
    fun getNomenclatureList(): Call<List<NomenclatureCollection>>

    @GET("rest/user/category/all")
    fun getAllCategory(): Call<List<Category>>

    @GET("rest/user/collection/category/{id}")
    fun getCollectionByCategory(@Path("id") id: String): Call<List<NomenclatureCollection>>

    @GET("rest/user/nomenclature/collection/{id}")
    fun getNomenclatureByCollection(@Path("id") id: String): Call<List<Nomenclature>>

    @GET("rest/user/partner/all")
    fun getAllPartner(): Call<List<Partner>>

    @POST("rest/user/auth")
    fun login(@Body login: LoginBody): Call<ServerToken>

}
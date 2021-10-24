package com.example.nordside_mobile.api

import com.example.nordside_mobile.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NordsideApi {

    @GET("rest/user/nomenclature/all?email=user@gmail.com")
    suspend fun getAllNomenclature(): List<PriceTable>

    @GET("rest/user/personal/nomenclature/collection/{id}")
    fun getPersonalNomenclatureListByCollection(@Path("id") id: String): Call<List<PriceTable>>

    @GET("rest/user/nomenclature/all?email=user@gmail.com")
    suspend fun getNomenclatureList(): List<NomenclatureCollection>

    @GET("rest/user/category/all")
    suspend fun getAllCategory(): List<Category>

    @GET("rest/user/collection/category/{id}")
    suspend fun getCollectionByCategory(@Path("id") id: String): List<NomenclatureCollection>

    @GET("rest/user/nomenclature/collection/{id}")
    suspend fun getNomenclatureByCollection(@Path("id") id: String): List<Nomenclature>

    @GET("rest/user/partner/all")
    suspend fun getAllPartner(): List<Partner>

    @POST("rest/user/auth")
    suspend fun login(@Body login: LoginBody): ServerToken

}
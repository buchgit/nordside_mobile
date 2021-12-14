package com.example.nordside_mobile.api

import com.example.nordside_mobile.model.*
import com.example.nordside_mobile.repository.BaseApiRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NordsideApi {

    @GET("rest/user/nomenclature/all?email=user@gmail.com")
    suspend fun getAllNomenclature(): List<PriceTable>

    @GET("rest/user/personal/nomenclature/collection/{id}")
    suspend fun getPersonalNomenclatureListByCollection(@Path("id") id: String): List<PriceTable>

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

    @POST("rest/user/refresh")
    suspend fun refreshToken(): ServerToken

    @POST("rest/user/order/create")
    suspend fun saveOrderOnServer(@Body order:Order):String

    @GET("rest/user/order/all")
    suspend fun getOrderList(): List<Order>

    suspend fun register(@Body login: LoginBody): Boolean
}
package com.example.nordside_mobile.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.nordside_mobile.BuildConfig
import com.example.nordside_mobile.MyApp
import com.example.nordside_mobile.api.NordsideApi
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.database.NordsideDataBase
import com.example.nordside_mobile.utils.ApplicationConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BaseModule {


    // Retrofit API
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            //.client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideNordsideApi(retrofit: Retrofit): NordsideApi =
        retrofit.create(NordsideApi::class.java)

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



    // DataBase
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): NordsideDataBase =
        Room.databaseBuilder(
            context.applicationContext,
            NordsideDataBase::class.java,
            "nordside database"
        ).build()

    @Singleton
    @Provides
    fun provideCartDao(database: NordsideDataBase): CartDao =
        database.cartDao()

}
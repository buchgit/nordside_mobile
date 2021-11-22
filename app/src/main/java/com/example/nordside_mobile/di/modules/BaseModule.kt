package com.example.nordside_mobile.di.modules

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import androidx.work.WorkerParameters
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.BuildConfig
import com.example.nordside_mobile.api.NordsideApi
import com.example.nordside_mobile.dao.CartDao
import com.example.nordside_mobile.database.NordsideDataBase
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.usecases.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseModule {

    private val TAG = "BaseModule ###"

    // Retrofit API
    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun provideRetrofit(
        accessTokenUseCase: AccessTokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase
    )
            : Retrofit {

        var token: String? = null
        if (!accessTokenUseCase.isExpared) {
            token = accessTokenUseCase.token
            Log.v(TAG, "access token is no expired")
        } else if (!refreshTokenUseCase.isExpared) {
            token = refreshTokenUseCase.token
            Log.v(TAG, "### refresh token is no expired $token")
        } else {
            Log.v(TAG, "Both tokens is expired or null")
        }//TODO if both tokens is expired needs anything to do

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Singleton
    @Provides
    fun provideNordsideApi(retrofit: Retrofit): NordsideApi =
        retrofit.create(NordsideApi::class.java)

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

    @Singleton
    @Provides
    fun provideAppPreference(@ApplicationContext appContext: Context): AppPreference {
        return AppPreference(appContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun provideAccessToken(appSetting: AppPreference): AccessTokenUseCase {
        return AccessTokenUseCase(appSetting.getSavedString(ApplicationConstants().ACCESS_TOKEN) ?:"")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun provideRefreshToken(appSetting: AppPreference): RefreshTokenUseCase {
        return RefreshTokenUseCase(appSetting.getSavedString(ApplicationConstants().REFRESH_TOKEN) ?:"")
    }

    @Provides
    fun provideLoginBody(): LoginBody {
        return LoginBody()
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    @Provides
//    fun provideCheckTokenUseCase(
//        @ApplicationContext appContext: Context,
//        workerParams: WorkerParameters,
//        paramGetTokenUseCase: GetTokenUseCase
//    ): CheckTokenUseCase {
//        return CheckTokenUseCase(appContext, workerParams, paramGetTokenUseCase)
//    }

}
package com.example.nordside_mobile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.usecases.AccessTokenUseCase
import com.example.nordside_mobile.usecases.ApplicationConstants
import com.example.nordside_mobile.usecases.CheckTokenUseCase
import com.example.nordside_mobile.usecases.GetTokenUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidApp
open class MyApp : Application(), Configuration.Provider {

    private val TAG = "${MyApp::class.java.simpleName} ###"

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    var tokenRefreshRequest = PeriodicWorkRequest.Builder(
        CheckTokenUseCase::class.java, 15,
        java.util.concurrent.TimeUnit.MINUTES
    ).build()

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"onCreate()")
        WorkManager.getInstance(applicationContext).enqueue(tokenRefreshRequest)
    }

}



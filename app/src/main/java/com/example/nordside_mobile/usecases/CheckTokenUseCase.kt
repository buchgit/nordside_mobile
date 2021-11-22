package com.example.nordside_mobile.usecases

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.MyApp
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltWorker
class CheckTokenUseCase @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
    //paramGetTokenUseCase: GetTokenUseCaseInt
) : Worker(appContext, workerParams) {

    private val TAG = "${CheckTokenUseCase::class.java.simpleName} ###"

    private val appPreference: SharedPreferences = appContext.getSharedPreferences(
        ApplicationConstants().SHARED_PREFERENCES_FILE,
        Context.MODE_PRIVATE
    )

    //var getTokenUseCase = paramGetTokenUseCase as GetTokenUseCase

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GetTokenUseCaseInt {
        fun getTokenUseCase(): GetTokenUseCase
    }

    val getTokenUseCaseInt =
        EntryPointAccessors.fromApplication(appContext, GetTokenUseCaseInt::class.java)

    var getTokenUseCase = getTokenUseCaseInt.getTokenUseCase()

        @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.v(TAG,"doWork()")
        val accessTokenUseCase = appPreference.getString(ApplicationConstants().ACCESS_TOKEN, "")
            ?.let { AccessTokenUseCase(it) }
        if (accessTokenUseCase != null) {
            if (accessTokenUseCase.isExpared) {
                launchRefreshToken()
            }
        }
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun launchRefreshToken() = runBlocking {
        launch {
            Log.v(TAG,"launchRefreshToken()")
            getTokenUseCase.execute(LoginBody("user@gmail.com","user"))
        }
    }

//    @HiltWorker
//    class ExampleWorker @AssistedInject constructor(
//        @Assisted appContext: Context,
//        @Assisted workerParams: WorkerParameters,
//        workerDependency: WorkerDependency
//    ) : Worker(appContext, workerParams) { ... }

}
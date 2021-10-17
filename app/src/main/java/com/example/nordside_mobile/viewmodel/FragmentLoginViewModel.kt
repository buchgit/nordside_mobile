package com.example.nordside_mobile.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.usecases.GetTokenUseCase
import com.example.nordside_mobile.usecases.LoginCheckerUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Переписать, чтобы репозиторий передавался в фабрике в конструкторе
class FragmentLoginViewModel() : ViewModel() {

    private val repositoryApi: NordsideRepository = NordsideRepository.get()
    private var tokenLiveData : LiveData<ServerToken>? = null

    suspend fun logIn(loginBody: LoginBody, context: Context) : LiveData<ServerToken>? {
        return GetTokenUseCase.newInstance().execute(
            loginBody,
            repositoryApi,
            context
        )
//        return repositoryApi.login(loginBody)
    }

    suspend fun loginBodyChecker(loginBody: LoginBody) : Boolean {
        val isCorrectLoginDeferred = viewModelScope.async {
            LoginCheckerUseCase.newInstance().execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

}
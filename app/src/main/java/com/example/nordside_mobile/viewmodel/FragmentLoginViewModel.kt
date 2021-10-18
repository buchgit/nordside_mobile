package com.example.nordside_mobile.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.usecases.GetTokenUseCase
import com.example.nordside_mobile.usecases.LoginValidatorUseCase
import com.example.nordside_mobile.usecases.ValidateState
import kotlinx.coroutines.async

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

    suspend fun loginBodyChecker(loginBody: LoginBody) : ValidateState {
        val isCorrectLoginDeferred = viewModelScope.async {
            LoginValidatorUseCase.newInstance().execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

}
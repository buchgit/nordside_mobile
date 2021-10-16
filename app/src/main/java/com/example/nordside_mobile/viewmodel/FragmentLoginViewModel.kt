package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.usecases.LoginCheckerUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Переписать, чтобы репозиторий передавался в фабрике в конструкторе
class FragmentLoginViewModel() : ViewModel() {

    private val repositoryApi: NordsideRepository = NordsideRepository.get()
    private var tokenLiveData : LiveData<ServerToken>? = null

    fun logIn(loginBody: LoginBody) : LiveData<ServerToken> {
        return repositoryApi.login(loginBody)
    }

    suspend fun loginBodyChecker(loginBody: LoginBody) : Boolean {
        val isCorrectLoginDeferred = viewModelScope.async {
            LoginCheckerUseCase.newInstance().execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

}
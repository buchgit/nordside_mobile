package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.usecases.GetTokenUseCase
import com.example.nordside_mobile.usecases.LoginValidatorUseCase
import com.example.nordside_mobile.usecases.ValidateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val loginValidatorUseCase: LoginValidatorUseCase
) : ViewModel() {

    suspend fun logIn(loginBody: LoginBody) : LiveData<ServerToken>? {
        return getTokenUseCase.execute(loginBody)
    }

    suspend fun loginBodyChecker(loginBody: LoginBody) : ValidateState {
        val isCorrectLoginDeferred = viewModelScope.async {
            loginValidatorUseCase.execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

}
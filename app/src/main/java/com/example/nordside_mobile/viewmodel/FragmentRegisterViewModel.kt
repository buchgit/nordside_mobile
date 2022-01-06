package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.usecases.ValidateState
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.usecases.LoginValidatorUseCase
import com.example.nordside_mobile.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class FragmentRegisterViewModel @Inject constructor(
    private val loginValidatorUseCase: LoginValidatorUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    suspend fun loginBodyChecker(loginBody: LoginBody) : ValidateState {
        val isCorrectLoginDeferred = viewModelScope.async {
            loginValidatorUseCase.execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

    suspend fun register(loginBody: LoginBody) : Resource<Boolean>{
        return registerUseCase.execute(loginBody)
    }
}
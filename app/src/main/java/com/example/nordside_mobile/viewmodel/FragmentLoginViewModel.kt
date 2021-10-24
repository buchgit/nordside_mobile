package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken

import com.example.nordside_mobile.repository.Resource

import com.example.nordside_mobile.usecases.ApplicationConstants

import com.example.nordside_mobile.usecases.GetTokenUseCase
import com.example.nordside_mobile.usecases.LoginValidatorUseCase
import com.example.nordside_mobile.usecases.ValidateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val loginValidatorUseCase: LoginValidatorUseCase,
    private val appSetting: AppPreference
) : ViewModel() {

    private var _tokenLiveData: MutableLiveData<Resource<ServerToken>> = MutableLiveData()
    val tokenLiveData: LiveData<Resource<ServerToken>> get() = _tokenLiveData

    suspend fun logIn(loginBody: LoginBody) : LiveData<Resource<ServerToken>> {
        val tokenResource = getTokenUseCase.execute(loginBody)
        if (tokenResource is Resource.Success) {
            _tokenLiveData.value = tokenResource
        }
        return tokenLiveData
    }

    suspend fun loginBodyChecker(loginBody: LoginBody) : ValidateState {
        val isCorrectLoginDeferred = viewModelScope.async {
            loginValidatorUseCase.execute(loginBody)
        }
        return isCorrectLoginDeferred.await()
    }

    fun getTokenFromSharedPreferences():String?{
        return appSetting.getSavedString(ApplicationConstants().TOKEN)
    }



}
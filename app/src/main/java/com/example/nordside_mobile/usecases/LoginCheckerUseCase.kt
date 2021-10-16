package com.example.nordside_mobile.usecases

import com.example.nordside_mobile.model.LoginBody

class LoginCheckerUseCase {

    suspend fun execute(loginBody: LoginBody) : Boolean {
        return true
    }

    companion object {
        fun newInstance() = LoginCheckerUseCase()
    }
}
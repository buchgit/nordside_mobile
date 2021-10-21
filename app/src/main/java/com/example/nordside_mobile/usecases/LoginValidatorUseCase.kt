package com.example.nordside_mobile.usecases

import com.example.nordside_mobile.model.LoginBody
import javax.inject.Inject

class LoginValidatorUseCase @Inject constructor() {

    suspend fun execute(loginBody: LoginBody) : ValidateState {

        val validateState = ValidateState(
            ValidateState.EmailState.OK,
            ValidateState.PasswordState.OK
        )

        with(loginBody) {
            if (email.isEmpty()) {
                validateState.emailState = ValidateState.EmailState.EMPTY
            }else if (!email.matches("""[a-zA-Z0-9._]+@[a-z]+\.+[a-z]+""".toRegex())) {
                validateState.emailState = ValidateState.EmailState.INCORRECT
            }else if (email.length < 6) {
                validateState.emailState = ValidateState.EmailState.SMALL
            }

            if (password.isEmpty()) {
                validateState.passwordState = ValidateState.PasswordState.EMPTY
            }else if (password.length < 5) {
                validateState.passwordState = ValidateState.PasswordState.SMALL
            }
        }
        return validateState
    }

}

data class ValidateState (
    var emailState: EmailState,
    var passwordState: PasswordState
) {
    sealed class EmailState {
        object EMPTY: EmailState()
        object INCORRECT: EmailState()
        object SMALL: EmailState()
        object OK: EmailState()
    }
    sealed class PasswordState {
        object EMPTY: PasswordState()
        object SMALL: PasswordState()
        object OK: PasswordState()
    }
}





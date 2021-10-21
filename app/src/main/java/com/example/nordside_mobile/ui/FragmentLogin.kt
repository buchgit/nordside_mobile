package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentLoginBinding
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.usecases.ValidateState
import com.example.nordside_mobile.viewmodel.FragmentLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentLogin : Fragment(R.layout.fragment_login) {

    private val TAG = "${FragmentLogin::class.simpleName} ###"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentLoginViewModel by viewModels()
    private var callbacks: Callback? = null
    private lateinit var loginBody: LoginBody

    companion object {
        fun createArgs() = bundleOf(
        )
    }

    interface Callback {
        fun onLoginClicked(isCorrectLogin: Boolean)
        fun onRegistrationClicked(login: LoginBody)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        with(binding) {
            buttonLogin.setOnClickListener() { loginButtonListener() }
            buttonRegistration.setOnClickListener { registrationButtonListener() }
        }
    }

    private fun loginButtonListener() {
        loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        Log.v(TAG, "login ${loginBody.email} ${loginBody.password}")

        viewModel.viewModelScope.launch {
            val validateState = viewModel.loginBodyChecker(loginBody)
            with(validateState) {

                if (emailState == ValidateState.EmailState.OK
                    && passwordState == ValidateState.PasswordState.OK) {

                    val tokenLiveData = viewModel.logIn(loginBody)
                    if (tokenLiveData != null) {
                        callbacks?.onLoginClicked(true)
                    }
                }

                when (emailState) {
                    ValidateState.EmailState.EMPTY -> {
                        binding.etEmailContainer.error = getString(R.string.email_empty)
                    }
                    ValidateState.EmailState.INCORRECT -> {
                        binding.etEmailContainer.error = getString(R.string.email_incorrect)
                    }
                    ValidateState.EmailState.SMALL -> {
                        binding.etEmailContainer.error = getString(R.string.email_small)
                    }
                }

                when (passwordState) {
                    ValidateState.PasswordState.EMPTY -> {
                        binding.etPasswordContainer.error = getString(R.string.password_empty)
                    }
                    ValidateState.PasswordState.SMALL -> {
                        binding.etPasswordContainer.error = getString(R.string.password_small)
                    }
                }

            }
        }
    }

    private fun registrationButtonListener() {
        loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        callbacks?.onRegistrationClicked(loginBody)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        _binding = null
    }

}
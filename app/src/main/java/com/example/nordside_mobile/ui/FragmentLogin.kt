package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentLoginBinding
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.usecases.ApplicationConstants
import com.example.nordside_mobile.usecases.ValidateState
import com.example.nordside_mobile.viewmodel.FragmentLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLogin : Fragment(R.layout.fragment_login) {

    private val TAG = "${FragmentLogin::class.simpleName} ###"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentLoginViewModel by viewModels()
    private var callbacks: Callback? = null
    private lateinit var loginBody: LoginBody
    @Inject lateinit var appPreference: AppPreference

    companion object {
        fun createArgs() = bundleOf(
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = appPreference.getSavedString(ApplicationConstants().ACCESS_TOKEN)
        val refreshToken = appPreference.getSavedString(ApplicationConstants().REFRESH_TOKEN)

        token?.let{
            //ifTokenExist()  //TODO Временно закомментил, так как переключаюсь между разными серверами с разными парами токенов
        }
    }


    interface Callback {
        fun onLoginClicked(isCorrectLogin: Boolean)
        fun onRegistrationClicked(login: LoginBody)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        with(binding) {
            buttonLogin.setOnClickListener() { loginButtonListener() }
            buttonRegistration.setOnClickListener { registrationButtonListener() }
            val textViewToken = binding.twToken

            //TODO удалить после отладки
            textViewToken.setText(viewModel.getTokenFromSharedPreferences() ?: "token is null")

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loginButtonListener() {
        loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        Log.v(TAG, "login ${loginBody.email} ${loginBody.password}")

        viewModel.viewModelScope.launch {
            val validateState = viewModel.loginBodyChecker(loginBody)

            if (validateState.emailState == ValidateState.EmailState.OK
                && validateState.passwordState == ValidateState.PasswordState.OK) {

                showProgressBar()
                val tokenLiveData = viewModel.logIn(loginBody)
                hideProgressBar()

                when (tokenLiveData.value) {
                    is Resource.Success -> {
                        Log.v(TAG, "Resource.Success ${tokenLiveData.value}")
                        callbacks?.onLoginClicked(true)
                    }
                    is Resource.Error -> {
                        showErrorMessage(
                            (tokenLiveData.value as Resource.Error<ServerToken>).message
                        )
                        Log.v(TAG, "Resource.Error ${tokenLiveData.value}")
                    }
                }

            }

            when (validateState.emailState) {
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

            when (validateState.passwordState) {
                ValidateState.PasswordState.EMPTY -> {
                    binding.etPasswordContainer.error = getString(R.string.password_empty)
                }
                ValidateState.PasswordState.SMALL -> {
                    binding.etPasswordContainer.error = getString(R.string.password_small)
                }
            }


        }
    }

    private fun showErrorMessage(message: String?) {

    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }

    private fun ifTokenExist() {
        findNavController().navigate(R.id.action_fragmentLogin_to_fragmentPersonal)
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
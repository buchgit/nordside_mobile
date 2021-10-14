package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.LoginBody
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FragmentLogin : Fragment() {

    private val TAG = "${FragmentLogin::class.simpleName} ###"

    //private val viewModel by viewModels<FragmentLoginViewModel>()
    private lateinit var editTextEmail: TextInputEditText;
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: MaterialButton
    private lateinit var buttonRegistration: MaterialButton
    private var callbacks: Callback? = null
    private lateinit var login: LoginBody

    companion object {
        fun newInstance(): FragmentLogin {
            return FragmentLogin()
        }
    }

    interface Callback {
        fun onLoginClicked(login: LoginBody)
        fun onRegistrationClicked(login: LoginBody)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        editTextEmail = view.findViewById(R.id.et_email)
        editTextPassword = view.findViewById(R.id.et_password)
        buttonLogin = view.findViewById(R.id.button_login)
        buttonRegistration = view.findViewById(R.id.button_registration)

        buttonLogin.setOnClickListener() {
            //Toast.makeText(context,"Авторизация",Toast.LENGTH_SHORT).show()
            login = LoginBody(editTextEmail.text.toString(), editTextPassword.text.toString())

            Log.v(TAG, "login ${login.email} ${login.password}")

            callbacks?.onLoginClicked(login)
        }

        buttonRegistration.setOnClickListener {
            login = LoginBody(editTextEmail.text.toString(), editTextPassword.text.toString())
            callbacks?.onRegistrationClicked(login)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}
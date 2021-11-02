package com.example.soundboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private lateinit var emailInput: TextView
    private lateinit var passwordInput: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginRedirect: TextView
    private val SAVED_REGISTER_EMAIL = "register_email_key"
    private val SAVED_REGISTER_PASSWORD = "register_password_key"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        emailInput = view.findViewById(R.id.registerEmail) as TextView
        //emailInput.setSaveEnable(false)
        //passwordInput = view.findViewById(R.id.registerPassword)
        registerButton = view.findViewById(R.id.registerButton)
        loginRedirect = view.findViewById(R.id.loginHere)

        // TODO: Use the ViewModel

        //saves text on text change
        if(savedInstanceState != null){

            requireActivity().runOnUiThread { emailInput.text = "aaaaa"}
            emailInput.text = "aaaaa"
            println("saved not null")
            println("saved email:" + savedInstanceState.getString(SAVED_REGISTER_EMAIL))
        }

        /*
        emailInput.doOnTextChanged { inputText, start, before, count ->
            viewModel.email.value = inputText.toString()

            println("hello")
        }

        viewModel.email.observe(viewLifecycleOwner, {it ->
            println("observed change")
            if(!emailInput.text.toString().equals(it)) {
                emailInput.setText(it)
            }
        })


         */


    }

    //whyyy
    override fun onResume() {
        super.onResume()
        //emailInput.text =
    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_REGISTER_EMAIL, emailInput.text.toString() )
        println("email: " + emailInput.text.toString())
        super.onSaveInstanceState(outState)
    }


}
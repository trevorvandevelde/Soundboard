package com.example.soundboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var loginRedirect: TextView
    private val SAVED_REGISTER_EMAIL = "register_email_key"
    private val SAVED_REGISTER_PASSWORD = "register_password_key"
    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        emailLayout = view.findViewById(R.id.registerEmailLayout)
        passwordLayout = view.findViewById(R.id.registerPasswordLayout)
        confirmPasswordLayout = view.findViewById(R.id.registerConfirmPasswordLayout)
        registerButton = view.findViewById(R.id.registerButton)
        loginRedirect = view.findViewById(R.id.loginHere)

        registerButton.setOnClickListener { view ->
            emailLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            val email = emailLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()
            val confirmPassword = confirmPasswordLayout.editText?.text.toString()


            if(isEmailValid(email) && isPasswordValid(password) && confirmPassword.equals(password)){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        println("success!")
                    } else {
                        println("not successful")
                        it.exception
                    }
                }
            }else{
                if (!isEmailValid(email)) {
                    emailLayout.error = "Invalid email"
                }
                if (!isPasswordValid(password)) {
                    passwordLayout.error = "Invalid password: minimum 8 characters and 1 digit"
                }
                if(!confirmPassword.equals(password)){
                    confirmPasswordLayout.error = "Passwords do not match"
                }
            }
        }

    }

    public fun isEmailValid(email :String) : Boolean{
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    //at least 8 characters with 1 special char and one digit
    public fun isPasswordValid(password : String): Boolean{
        if(password.isNotBlank()) {
            val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$"
            val matcher = Regex(pattern)
            return matcher.find(password) != null
        }else{
            return false
        }

    }

    //saves text on text change
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_REGISTER_EMAIL, emailLayout.editText?.text.toString() )
        outState.putString(SAVED_REGISTER_PASSWORD, passwordLayout.editText?.text.toString())
        println("email: " + emailLayout.editText?.text.toString())
        super.onSaveInstanceState(outState)
    }

    //TODO: should set text, but settext not working in if even when not null
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //emailLayout.text = "test"
        if(savedInstanceState != null) {
            emailLayout.editText?.setText( savedInstanceState.getString(SAVED_REGISTER_EMAIL) )
            println("save: "+ savedInstanceState.getString(SAVED_REGISTER_EMAIL))
        }
    }


}
package com.example.soundboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.Intent
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/*
* Fragment to handle login form display and validation
* */
class LoginFragment : Fragment() {

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var registerRedirect: TextView
    private lateinit var mAuth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        emailLayout = view.findViewById(R.id.loginEmailLayout)
        passwordLayout = view.findViewById(R.id.loginPasswordLayout)
        loginButton = view.findViewById(R.id.loginButton)
        registerRedirect = view.findViewById(R.id.registerHere)

        //button setup
        loginButton.setOnClickListener {
            emailLayout.error = null
            passwordLayout.error = null

            val email = emailLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()

            loginUser(email, password)
        }

        registerRedirect.setOnClickListener{
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, RegisterFragment() )
                addToBackStack(null)
                commit()
            }
        }

    }


    /*
     * Logs in user
     * Once logged in, can get user data with mAuth.currentUser anywhere, user data will hold when app closes
     * Only cleared on mAuth.signOut()
     */
    private fun loginUser(email : String, password : String){
        emailLayout.error = null
        passwordLayout.error = null

        if(email.isNotBlank() && Util.isEmailValid(email) && password.isNotBlank()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser = mAuth.currentUser!!
                    Util.showToast(requireContext(), "Successfully logged in")
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Util.showToast(requireContext(), "${it.exception?.message}")
                }
            }
        }else{
            if(email.isBlank()){
                emailLayout.error = "Email cannot be blank"
            }else if(!Util.isEmailValid(email)){
                emailLayout.error = "Not a valid email address"
            }
            if(password.isBlank()){
                passwordLayout.error = "Password cannot be blank"
            }
        }
    }


}
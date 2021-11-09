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

        /*
        //test
        val user : FirebaseUser? = mAuth.currentUser
        if(user == null){
            println("not logged in")
        }else{
            println("Logged in " + user.uid + user.email)
        }
        */

        emailLayout = view.findViewById(R.id.loginEmailLayout)
        passwordLayout = view.findViewById(R.id.loginPasswordLayout)
        loginButton = view.findViewById(R.id.loginButton)
        registerRedirect = view.findViewById(R.id.registerHere)

        loginButton.setOnClickListener {
            emailLayout.error = null
            passwordLayout.error = null

            val email = emailLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()

            loginUser(email, password)
        }

        registerRedirect.setOnClickListener{
            //Fragment fragment = fm.findFragmentByTag("TagName")
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, RegisterFragment() )
                addToBackStack(null)
                commit()
            }
        }

    }


    /* logs in user
     * once logged in, can get user data with mAuth.currentUser anywhere
     * do not need to store, will hold when app closes
     * only cleared on mAuth.signOut()
     */
    private fun loginUser(email : String, password : String){
        emailLayout.error = null
        passwordLayout.error = null

        if(email.isNotBlank() && password.isNotBlank()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser = mAuth.currentUser!!
                    val userId = user.uid
                    val userEmail = user.email
                    Toast.makeText(requireContext(), "successfully logged in", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    //TODO: better login exception display, use errors?
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }else{
            if(email.isBlank()){
                emailLayout.error = "Email cannot be blank"
            }
            if(password.isBlank()){
                passwordLayout.error = "Password cannot be blank"
            }
        }
    }


}
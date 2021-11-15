package com.example.soundboard

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
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
import com.google.firebase.database.FirebaseDatabase

/**
 * Fragment to handle register form display and validation
 */
class RegisterFragment : Fragment() {

    private lateinit var emailLayout: TextInputLayout
    private lateinit var nicknameLayout: TextInputLayout
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

        nicknameLayout = view.findViewById(R.id.registerNicknameLayout)
        passwordLayout = view.findViewById(R.id.registerPasswordLayout)
        confirmPasswordLayout = view.findViewById(R.id.registerConfirmPasswordLayout)
        registerButton = view.findViewById(R.id.registerButton)
        loginRedirect = view.findViewById(R.id.loginHere)

        //button setup
        registerButton.setOnClickListener {
            val email = emailLayout.editText?.text.toString()
            val nickname = nicknameLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()
            val confirmPassword = confirmPasswordLayout.editText?.text.toString()

            registerUser(email, nickname, password, confirmPassword)
        }

        loginRedirect.setOnClickListener{
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, LoginFragment() )
                addToBackStack(null)
                commit()
            }
        }

    }


    //checks if user can register, displays validation errors, and calls firebase to register/login if valid
    private fun registerUser(email : String, nickname: String, password : String, confirmPassword : String){
        emailLayout.error = null
        nicknameLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null

        if(Util.isEmailValid(email) && Util.isNicknameValid(nickname) && Util.isPasswordValid(password) && confirmPassword.equals(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser = it.result!!.user!!
                    val newUserData = User()
                    newUserData.User(nickname)

                    //sets firebase key as uid
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.uid).setValue(newUserData)
                        .addOnCompleteListener{
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Util.showToast(requireContext(), "User registered")
                                    val intent = Intent(requireActivity(), MainActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    Util.showToast(requireContext(), "User registered, but failed to sign in")
                                }
                            }
                        }.addOnFailureListener{
                            Util.showToast(requireContext(), "Failed to register user: ${it.message}")
                        }


                } else {
                    Util.showToast(requireContext(), "${it.exception?.message}")
                }
            }
        }else{
            if (!Util.isEmailValid(email)) {
                emailLayout.error = "Invalid email"
            }
            if (!Util.isNicknameValid(nickname)) {
                val length = Util.NICKNAME_MAX_LENGTH
                nicknameLayout.error = "Nickname max length: {$length} characters"
            }
            if (!Util.isPasswordValid(password)) {
                passwordLayout.error = "Invalid password: minimum 8 characters and 1 digit"
            }
            if(!confirmPassword.equals(password)){
                confirmPasswordLayout.error = "Passwords do not match"
            }
        }
    }



    //saves text on orientation change. some issues
    //activity onSaveInstanceState will mess with this, avoid calling view stuff in here
    override fun onSaveInstanceState(outState: Bundle) {
        if(this::emailLayout.isInitialized) {
            outState.putString(SAVED_REGISTER_EMAIL, emailLayout.editText?.text.toString())
            outState.putString(SAVED_REGISTER_PASSWORD, passwordLayout.editText?.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null) {
            emailLayout.editText?.setText( savedInstanceState.getString(SAVED_REGISTER_EMAIL) )
        }
    }


}
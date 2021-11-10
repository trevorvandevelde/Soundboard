package com.example.soundboard

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
    private val nicknameMaxLength : Int = 24

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

        registerButton.setOnClickListener {
            val email = emailLayout.editText?.text.toString()
            val nickname = nicknameLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()
            val confirmPassword = confirmPasswordLayout.editText?.text.toString()

            registerUser(email, nickname, password, confirmPassword)
        }


        loginRedirect.setOnClickListener{
            //Fragment fragment = fm.findFragmentByTag("TagName")
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, LoginFragment() )
                addToBackStack(null)
                commit()
            }
        }

    }


    //checks if user can register, handles errors, and calls firebase to register if valid
    private fun registerUser(email : String, nickname: String, password : String, confirmPassword : String){
        emailLayout.error = null
        nicknameLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null

        if(isEmailValid(email) && isNicknameValid(nickname) && isPasswordValid(password) && confirmPassword.equals(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser = it.result!!.user!!
                    val newUserData = User()
                    newUserData.User(nickname)

                    //sets key as uid
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.uid).setValue(newUserData)
                        .addOnCompleteListener{
                            Toast.makeText(requireContext(), "Added User to Database", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(requireContext(), "Failed to add user to Database", Toast.LENGTH_SHORT).show()
                        }
                    Toast.makeText(requireContext(), "User registered", Toast.LENGTH_SHORT ).show()
                    //stored user info somewhere
                } else {
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT ).show()
                    //TODO: nicer register exception prompts
                }
            }
        }else{
            if (!isEmailValid(email)) {
                emailLayout.error = "Invalid email"
            }
            if (!isNicknameValid(nickname)) {
                nicknameLayout.error = "Nickname max length: {$nicknameMaxLength} characters"
            }
            if (!isPasswordValid(password)) {
                passwordLayout.error = "Invalid password: minimum 8 characters and 1 digit"
            }
            if(!confirmPassword.equals(password)){
                confirmPasswordLayout.error = "Passwords do not match"
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

    //nickname is less than certain number characters
    public fun isNicknameValid(nickname : String): Boolean{
        if(nickname.isNotBlank()) {
            return nickname.length < nicknameMaxLength
        }else{
            return false
        }

    }

    //saves text on orientation change. some issues
    //activity onSaveInstanceState will mess with this, avoid calling view stuff in here
    override fun onSaveInstanceState(outState: Bundle) {
        if(this::emailLayout.isInitialized) {
            outState.putString(SAVED_REGISTER_EMAIL, emailLayout.editText?.text.toString())
            outState.putString(SAVED_REGISTER_PASSWORD, passwordLayout.editText?.text.toString())
            println("email: " + emailLayout.editText?.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    //FIXME: should set text, but settext not working even when instancestate not null
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //emailLayout.text = "test"
        if(savedInstanceState != null) {
            emailLayout.editText?.setText( savedInstanceState.getString(SAVED_REGISTER_EMAIL) )
            println("save: "+ savedInstanceState.getString(SAVED_REGISTER_EMAIL))
        }
    }


}
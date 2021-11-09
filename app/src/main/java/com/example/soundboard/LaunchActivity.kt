package com.example.soundboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Launcher activity. Shows login/register
 * Checks firebase if user is logged in, if logged in, launch main activity
 */
class LaunchActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, loginFragment)
            addToBackStack(null)
            commit()
        }

        mAuth = FirebaseAuth.getInstance()
        val user : FirebaseUser? = mAuth.currentUser
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0) //no transition animation
        }

    }

    //no backs on login for simplicities sake
    override fun onBackPressed() {}
}
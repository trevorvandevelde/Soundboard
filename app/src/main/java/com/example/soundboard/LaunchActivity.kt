package com.example.soundboard

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Launcher activity. Shows login/register
 * Checks firebase if user is logged in, if logged in, launch main activity
 */
class LaunchActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var layout : CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, loginFragment)
            addToBackStack(null)
            commit()
        }

        window.statusBarColor = Color.TRANSPARENT
        layout = findViewById(R.id.container)
        val animDrawable = layout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()



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
package com.example.soundboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val profileFragment = ProfileFragment()

        textMessage = findViewById(R.id.message)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    textMessage.setText(R.string.title_home)
                    true
                }
                R.id.navigation_discover -> {
                    textMessage.setText(R.string.title_discover)
                    true
                }
                R.id.navigation_profile -> {
                    setCurrentFragment(profileFragment)
                    textMessage.setText(R.string.title_profile)
                    true
                }
                R.id.navigation_settings -> {
                    textMessage.setText(R.string.title_settings)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }
    }

}

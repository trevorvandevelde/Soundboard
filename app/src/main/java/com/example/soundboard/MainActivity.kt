package com.example.soundboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref : SharedPreferences
    private lateinit var fragments : ArrayList<Fragment>
    private var lastFragmentIndex : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val homeFragment = HomeFragment()
        val discoverFragment = DiscoverFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()

        fragments = ArrayList<Fragment>()
        fragments.add(homeFragment)
        fragments.add(discoverFragment)
        fragments.add(profileFragment)
        fragments.add(settingsFragment)

        if (savedInstanceState != null) {
            lastFragmentIndex = savedInstanceState.getInt("last_fragment_key")
        }
        setCurrentFragment(lastFragmentIndex)    //start on where you left off

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    setCurrentFragment(0)
                    true
                }
                R.id.navigation_discover -> {
                    setCurrentFragment(1)
                    true
                }
                R.id.navigation_profile -> {
                    setCurrentFragment(2)
                    true
                }
                R.id.navigation_settings -> {
                    setCurrentFragment(3)
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(index : Int) {
        lastFragmentIndex = index
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragments[index])
            commit()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("last_fragment_key", lastFragmentIndex)
        super.onSaveInstanceState(outState)
    }

}

package com.example.soundboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.soundboard.databinding.ActivityBoardBinding.inflate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref : SharedPreferences
    private lateinit var fragments : ArrayList<Fragment>
    private var lastFragmentIndex : Int = 0
    private lateinit var navView: BottomNavigationView
    private lateinit var layout : CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check_permission()
        navView = findViewById(R.id.nav_view)

        layout = findViewById(R.id.container)
        val animDrawable = layout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()


        // hide the navigation bar
//        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


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

//        binding = MainActivity.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        // adds the upload soundbyte floating action button (fab) here
        val fab: View = findViewById(R.id.uploadSoundbyteFab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, UploadSoundByteActivity::class.java)
            startActivity(intent)
        }

        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT

//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            window.statusBarColor = Color.TRANSPARENT
//        }

        val fragmentString = intent.extras?.getString("fragmentStart")
        if (fragmentString == "ProfileFragment") {
            setCurrentFragment(2)
        }

    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }


    /**
     * Changes fragment with fragment manager and makes sure last fragment index/position is saved
     */
    private fun setCurrentFragment(index : Int) {
        lastFragmentIndex = index
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragments[index])
            addToBackStack(null)
            commit()
        }
    }

    /* overrides back press to have instagram-esqe functionality
     * back on homescreen exits, back on anything else goes to home
     */
    override fun onBackPressed() {
        if( navView.selectedItemId == R.id.navigation_home ){
            finishAffinity() //exits all activites
        }else{
            navView.selectedItemId = R.id.navigation_home
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("last_fragment_key", lastFragmentIndex)
        super.onSaveInstanceState(outState)
    }

    fun check_permission(): Boolean {
        if (Build.VERSION.SDK_INT < 23){
            return false
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                0)
        }
        return true
    }

}

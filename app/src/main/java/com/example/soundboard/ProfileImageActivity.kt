package com.example.soundboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

// used to choose the user's profile image
class ProfileImageActivity: AppCompatActivity() {

    private lateinit var image_choose_recyclerview: RecyclerView
    private var datalist = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_image_layout)

        // adds back button fab
        val backButton: View = findViewById(R.id.back_arrow)
        backButton.setOnClickListener { view ->
            finish()
        }

        val headername: TextView = findViewById(R.id.header_name)
        headername.setText("Choose a Profile Image")

        // add all of the local pre-provided potential profile images
        val local_images = Arrays.asList(R.drawable.profile_icon_1,
            R.drawable.profile_icon_2, R.drawable.profile_icon_3,
            R.drawable.profile_icon_4, R.drawable.profile_icon_5,
            R.drawable.profile_icon_6, R.drawable.profile_icon_7,
            R.drawable.profile_icon_8, R.drawable.profile_icon_9,
            R.drawable.profile_icon_10)

        datalist.addAll(local_images)

        // set these profile images in the recyclerview
        image_choose_recyclerview = findViewById(R.id.image_recyclerview)
        val adapter = ProfileImageAdapter(datalist)
        image_choose_recyclerview.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        image_choose_recyclerview.layoutManager = layoutManager
    }

}
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

class ProfileImageActivity: AppCompatActivity() {

    private lateinit var image_choose_recyclerview: RecyclerView
    private var datalist = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_image_layout)

        val headername: TextView = findViewById(R.id.header_name)
        headername.setText("Choose a Profile Image")

        val local_images = Arrays.asList(R.drawable.profile_icon_1,
            R.drawable.profile_icon_2, R.drawable.profile_icon_3,
            R.drawable.profile_icon_4, R.drawable.profile_icon_5,
            R.drawable.profile_icon_6, R.drawable.profile_icon_7,
            R.drawable.profile_icon_8, R.drawable.profile_icon_9)

        datalist.addAll(local_images)

        image_choose_recyclerview = findViewById(R.id.image_recyclerview)
        val adapter = ProfileImageAdapter(datalist)
        image_choose_recyclerview.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        image_choose_recyclerview.layoutManager = layoutManager
    }

}
package com.example.soundboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class BoardActivity : AppCompatActivity(){

    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var board_listview: ListView
    private var datalist = ArrayList<SoundByteEntry>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        if(datalist.size == 0) {
            initData()
        }

        board_listview = findViewById(R.id.board_listview)
        soundbyteAdapter = SoundbyteAdapter(this, R.layout.soundbyte_item, datalist)
        board_listview.adapter = soundbyteAdapter
        board_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageID)
            intent.putExtra("title", soundbyte.title)
            startActivity(intent)
        }
    }

    private fun initData(){
        repeat(10){
            datalist.add(SoundByteEntry( R.drawable.dartmouth,"Test"))
        }
    }

}
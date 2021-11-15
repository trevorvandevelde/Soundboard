package com.example.soundboard

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.fragment.app.DialogFragment


class BoardActivity : AppCompatActivity(){

    private lateinit var soundboard_event_listener: ValueEventListener
    private lateinit var  soundboard_reference: DatabaseReference

    private lateinit var audio_event_listener: ValueEventListener
    private lateinit var  audio_reference: DatabaseReference

    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var board_listview: ListView
    private lateinit var board_name_view: TextView
    private var datalist = ArrayList<SoundByteEntry>()
    private lateinit var board_position : String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        board_name_view = findViewById(R.id.header_name)
        val board_name = intent.getStringExtra("title")
        board_position = intent.getStringExtra("position").toString()
        if(board_name != null){
            board_name_view.setText(board_name)
        }


        // adds back button fab
        val backButton: View = findViewById(R.id.back_arrow)
        backButton.setOnClickListener { view ->
            finish()
        }

        if(datalist.size == 0) {
            //initData()
            retrieve_audio()
        }

        board_listview = findViewById(R.id.board_listview)
        soundbyteAdapter = SoundbyteAdapter(this, R.layout.soundbyte_item, datalist)
        board_listview.adapter = soundbyteAdapter

        board_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageUrl)
            intent.putExtra("title", soundbyte.title)
            intent.putExtra("audio", soundbyte.audioUrl)
            intent.putExtra("soundByteId", soundbyte.id )
            var tags:ArrayList<String> = ArrayList<String>()
            for(item in soundbyte.tag_list){
                tags.add(item)
            }
            intent.putStringArrayListExtra("tags", tags)
            startActivity(intent)
        }

        board_listview.setOnItemLongClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            //println(soundbyte.id)
            //println(board_position)
            //println(board_name)
            val soundByteDialogDelete = Delete_Soundbyte_Dialog()
            //println(id.toString())
            val bundleId = Bundle()
            bundleId.putString("soundByteId", soundbyte.id)
            bundleId.putString("board_position", board_position)
            bundleId.putString("board_name", board_name)
            soundByteDialogDelete.arguments = bundleId
            soundByteDialogDelete.show(getSupportFragmentManager(), "Delete_Soundbyte_Dialog")
            true
        }

    }

    //observes changes to selected soundboard's hashmap of soundbyteids, gets all audio once
    //gets soundbytes to display from audio using the soundbyte ids..
    private fun retrieve_audio(){
        val soundboardId = board_position //must change to match soundboard
        soundboard_reference = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("soundBoardList")
            .child(soundboardId)
            .child("soundByteIdMap")
        if(soundboard_reference != null){
            print("null")
        }

        soundboard_event_listener =  object : ValueEventListener {
            override fun onDataChange(soundboardIds: DataSnapshot) {

                val audio_ref = FirebaseDatabase.getInstance().getReference().child("Audio").addListenerForSingleValueEvent(
                    object : ValueEventListener {

                        override fun onDataChange(audio_snapshot: DataSnapshot) {
                        val users_ref = FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(
                            object : ValueEventListener {

                                override fun onDataChange(users_snapshot: DataSnapshot) {
                                    datalist.clear()
                                    for (id in soundboardIds.children) {
                                        println(id)
                                        if(audio_snapshot.hasChild(id.key!!)) {
                                            val song: SoundByte? = audio_snapshot.child(id.key!!).getValue(SoundByte::class.java)
                                            val user: User? =
                                                users_snapshot.child(song!!.getUploaderUserName())
                                                    .getValue(User::class.java)

                                            // for the safety
                                            val soundbyteId = id.key
                                            val username = user!!.getUserNickname()
                                            val imageurl = song!!.getImageUrl()
                                            val soundname = song!!.getSoundName()
                                            val duration = song!!.getDuration() + "s"
                                            val tags = song!!.getTags()
                                            val songurl = song!!.getSoundUrl()
                                            if (soundbyteId != null && username != null && imageurl != null && soundname != null && duration != null
                                                && tags != null && songurl != null
                                            ) {
                                                datalist.add(
                                                    SoundByteEntry(
                                                        soundbyteId, username, imageurl,
                                                        soundname, duration, tags, songurl
                                                    )
                                                )
                                            } else {
                                                datalist.add(SoundByteEntry())
                                            }
                                        }

                                    }
                                    soundbyteAdapter = SoundbyteAdapter(
                                        applicationContext,
                                        R.layout.soundbyte_item,
                                        datalist
                                    )
                                    board_listview.adapter = soundbyteAdapter
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }} )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "FAILED!", Toast.LENGTH_SHORT).show()
            }
        }
        soundboard_reference.addValueEventListener(soundboard_event_listener )
    }

    //remove listener on exiting, else new soundboards added notifies listener not attached to context
    override fun onDestroy() {
        soundboard_reference.removeEventListener(soundboard_event_listener)

        super.onDestroy()
    }

    private fun initData(){
        repeat(10){
            datalist.add(SoundByteEntry( "@ username", "NA","soundbyte title", "12s"))
        }
    }

}
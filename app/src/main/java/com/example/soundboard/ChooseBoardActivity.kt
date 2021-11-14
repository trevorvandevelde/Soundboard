package com.example.soundboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChooseBoardActivity : AppCompatActivity() {

    private lateinit var board_choose_recyclerview: RecyclerView
    private lateinit var board_name_view: TextView
    private val datalist = ArrayList<BoardEntry>()
    private lateinit var user_reference: DatabaseReference
    private lateinit var user_event_listener:  ValueEventListener
    private lateinit var createbutton_view: Button

    private lateinit var soundByteId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_board)


        val page_tite : TextView = findViewById(R.id.header_name)
        page_tite.setText("Choose a Board")

        createbutton_view = findViewById(R.id.add_board)
        createbutton_view.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Add_Board_Dialog().show(supportFragmentManager, "add_board_dialog")
                true
            }
        })

        soundByteId = intent.getStringExtra("soundByteId")!!

        board_choose_recyclerview = findViewById(R.id.board_choose_recyclerview)


        val backButton: View = findViewById(R.id.back_arrow)
        backButton.setOnClickListener { view ->
            finish()
        }

        user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //for (ds in snapshot.child("Audio").children)
                val user : User? =  snapshot.getValue(User::class.java)
                if(user != null){
                    datalist.clear()
                    val soundboards = user.getSoundBoardList()
                    for(sb in soundboards){
                        println(sb)
                        datalist.add(BoardEntry("NA", sb.getSoundBoardName(), "${sb.getSoundByteIdMap().size} soundbytes", sb.getSoundByteIdMap() ))
                    }

                    val adapter = BoardChooseAdapter(datalist, soundByteId)
                    board_choose_recyclerview.adapter = adapter
                    val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    board_choose_recyclerview.layoutManager = layoutManager
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        user_reference.addValueEventListener(user_event_listener)





    }

}



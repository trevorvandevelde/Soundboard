package com.example.soundboard

import android.app.ProgressDialog.show
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val datalist = ArrayList<BoardEntry>()
    private lateinit var discover_recyclerview : RecyclerView

    private lateinit var userDescription : TextView
    private lateinit var userNickname : TextView

    private lateinit var user_reference: DatabaseReference
    private lateinit var user_event_listener:  ValueEventListener
    private lateinit var createbutton_view: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        createbutton_view = view.findViewById(R.id.add_board)

        if(datalist.size == 0) {
            initData()
        }
        discover_recyclerview = view.findViewById(R.id.board_recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        discover_recyclerview.layoutManager = layoutManager
        val adapter = BoardAdapter(datalist)
        discover_recyclerview.adapter = adapter

        userNickname = view.findViewById(R.id.user_name)
        userDescription = view.findViewById(R.id.user_intro)

        user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //for (ds in snapshot.child("Audio").children)
                val user : User? =  snapshot.getValue(User::class.java)
                if(user != null){
                    userNickname.text = user.getUserNickname()
                    userDescription.text = user.getUserDescription()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        user_reference.addValueEventListener(user_event_listener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun initData(){
        repeat(10){
            datalist.add(BoardEntry( R.drawable.dartmouth,"Board name", "23 soundbytes"))
        }
    }

    override fun onDestroy() {
        user_reference.removeEventListener(user_event_listener)
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createbutton_view.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Add_Board_Dialog().show(childFragmentManager, "add_board_dialog")
                true
            }
        })
    }

}
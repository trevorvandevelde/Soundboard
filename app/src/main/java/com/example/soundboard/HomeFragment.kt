package com.example.soundboard

import android.R.attr
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.NonNull

import android.R.attr.thumbnail
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// import androidx.test.core.app.ApplicationProvider.getApplicationContext


// the page used to recommend audios from the firebase
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var discoverShortcut : ImageButton
    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var main_listview: ListView
    private lateinit var welcomeUserTv: TextView

    private  var audio_namelist: MutableList<String> = mutableListOf()
    private  var audio_urllist: MutableList<String> = mutableListOf()
    private  var audio_artisitlist: MutableList<String> = mutableListOf()
    private  var audio_durationlist: MutableList<String> = mutableListOf()
    private  var audio_coverlist: MutableList<String> = mutableListOf()
    private  var audio_taglists: MutableList<MutableList<String>> = mutableListOf()

    private var datalist = ArrayList<SoundByteEntry>()
    private lateinit var database_reference: DatabaseReference
    private lateinit var database_event_listener: ValueEventListener


    private lateinit var user_reference: DatabaseReference
    private lateinit var user_event_listener:  ValueEventListener
    private var local_data: MutableList<SoundByteEntry> = mutableListOf()
    private var datanumber = 5

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)

        discoverShortcut = view.findViewById(R.id.discover_shortcut)
        discoverShortcut.setOnClickListener{
            val discoverFragment: Fragment = DiscoverFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, discoverFragment).commit()
        }

        // set the title part
        welcomeUserTv = view.findViewById(R.id.welcomeUserTv)
        user_reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //for (ds in snapshot.child("Audio").children)
                val user : User? =  snapshot.getValue(User::class.java)
                if(user != null){
                    welcomeUserTv.text = "Hi, ${user.getUserNickname()}!"
                }else{
                    welcomeUserTv.text = "Welcome!"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        user_reference.addValueEventListener(user_event_listener)

        main_listview = view.findViewById(R.id.home_listview)
        retrieve_audio()
        /*
        // used to test locally
        if(datalist.size == 0) {
            //  initData()
        }

        else{
            soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item,datalist)
            main_listview.adapter = soundbyteAdapter
        }
*/

        // jump to the play page once click
        main_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(requireActivity(), PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageUrl)
            intent.putExtra("title", soundbyte.title)
            intent.putExtra("audio", soundbyte.audioUrl)
            intent.putExtra("soundByteId", soundbyte.id )
            intent.putExtra("author", soundbyte.author)

            var tags:ArrayList<String> = ArrayList<String>()
            for(item in soundbyte.tag_list){
                tags.add(item)
            }
            intent.putStringArrayListExtra("tags", tags)
            startActivity(intent)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    // used to test locally
    private fun initData() {
        val tag_list: MutableList<String> = mutableListOf("DIY", "SHIBUYA", "AKIHABARA", "GINZA")
        var time = 1
        repeat(10) {
            datalist.add(SoundByteEntry("NA", "@ username", "NA", time.toString(), "12s", tag_list))
            time++
        }
    }

    // downstream data from the firebase
    private fun retrieve_audio(){
        database_reference = FirebaseDatabase.getInstance().getReference().child("Audio")
        database_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //gets list of users only once, for names. so doesn't fetch data on every nickname change
                val users_ref = FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(users_snapshot: DataSnapshot) {
                            local_data.clear()
                            // store the data locally
                            for (ds in snapshot.children) {
                                val song:SoundByte?= ds.getValue(SoundByte::class.java)
                                val user : User? =  users_snapshot.child(song!!.getUploaderUserName()).getValue(User::class.java)

                                // for the safety
                                val soundbyteId = ds.key
                                val username = user!!.getUserNickname()
                                val imageurl = song!!.getImageUrl()
                                val soundname = song!!.getSoundName()
                                val duration = song!!.getDuration() + "s"
                                val tags = song!!.getTags()
                                val songurl = song!!.getSoundUrl()
                                if(soundbyteId !=null && username !=null && imageurl != null && soundname != null && duration != null
                                    && tags != null && songurl != null) {
                                    local_data.add(
                                        SoundByteEntry(
                                            soundbyteId, username, imageurl,
                                            soundname, duration, tags, songurl
                                        )
                                    )
                                }
                                else{
                                    local_data.add(SoundByteEntry())
                                }

                            }
                            // fresh the datas to the datalist
                            refresh_data()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "FAILED!", Toast.LENGTH_SHORT).show()
            }
        }
        database_reference.addValueEventListener(database_event_listener )
    }


    private fun refresh_data(){
        // fresh the datas to the datalist
        datalist.clear()
        // we recommend 5 audios randomly from the firebase
        for (i in 1..5 ){
            local_data.shuffled().take(1).forEach{
                datalist.add(it)
            }
        }
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item,datalist)
        main_listview.adapter = soundbyteAdapter
    }

    //remove listener on exiting, else new users added notifies listener not attached to context
    override fun onDestroy() {
        database_reference.removeEventListener(database_event_listener)
        user_reference.removeEventListener(user_event_listener)
        super.onDestroy()
    }


}
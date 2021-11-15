package com.example.soundboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*


class DiscoverFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var discover_listview: ListView
    private lateinit var discover_search: SearchView
    // to store all of the original data from the firebase
    private var datalist = ArrayList<SoundByteEntry>()
    private var querylist = ArrayList<SoundByteEntry>()

    private lateinit var database_reference: DatabaseReference
    private lateinit var database_event_listener: ValueEventListener
    private  var local_data: MutableList<SoundByteEntry> = mutableListOf()
    private var datanumber = 10
    private var is_query: Boolean = false
    private lateinit var navView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.discover_fragment, container, false)
        /*
        if(datalist.size == 0) {
            initData()
        }
         */
        retrieve_audio()
        discover_search = view.findViewById(R.id.discover_search)
        discover_listview = view.findViewById(R.id.discover_listview)
        //discover_listview.setTextFilterEnabled(true)
        //soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, datalist)
        //discover_listview.adapter = soundbyteAdapter
        discover_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = querylist[position]
            val intent = Intent(requireActivity(), PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageUrl)
            intent.putExtra("title", soundbyte.title)
            intent.putExtra("audio", soundbyte.audioUrl)
            intent.putExtra("soundByteId", soundbyte.id )
            intent.putExtra("author", soundbyte.author )
            var tags:ArrayList<String> = ArrayList<String>()
            for(item in soundbyte.tag_list){
                tags.add(item)
            }
            intent.putStringArrayListExtra("tags", tags)
            startActivity(intent)
        }

        discover_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String): Boolean{
                //soundbyteAdapter.filter.filter(query)
                is_query=true
                querylist.clear()
                var found:Boolean = false
                for(item in local_data) {
                    if (item.title.contains(query, ignoreCase = true)) {
                        querylist.add(item)
                        found = true
                    }
                    else{
                        for(tag in item.tag_list){
                            if(tag.contains(query, ignoreCase = true)||
                                    query.contains(tag, ignoreCase = true)){
                                querylist.add(item)
                                found = true
                                break
                            }
                        }
                    }
                }
                soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, querylist)
                discover_listview.adapter = soundbyteAdapter
                return found
            }

            override fun onQueryTextChange(query: String?): Boolean {
                //soundbyteAdapter.filter.filter(p0)
                is_query=true
                if(query == ""){
                    refresh_data()
                    return false
                }
                querylist.clear()
                var found:Boolean = false
                for(item in local_data) {
                    if (item.title.contains(query!!, ignoreCase = true)) {
                        querylist.add(item)
                        found = true
                    }
                    else if (item.author.contains(query!!, ignoreCase = true)){
                        querylist.add(item)
                        found = true
                    }
                    else{
                        for(tag in item.tag_list){
                            if(tag.contains(query, ignoreCase = true)){
                                querylist.add(item)
                                found = true
                                break
                            }
                        }
                    }
                }
                soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, querylist)
                discover_listview.adapter = soundbyteAdapter
                return found
            }

    })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView = requireActivity().findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_discover

        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }


    // used to test
    private fun initData(){
        val tag_list:MutableList<String> = mutableListOf("DIY","SHIBUYA","AKIHABARA","GINZA")
        var time = 1
        repeat(10){
            datalist.add(SoundByteEntry("NA","@ username", "NA",time.toString(), "12s",tag_list))
            time++
        }
    }

    private fun retrieve_audio(){
        database_reference = FirebaseDatabase.getInstance().getReference().child("Audio")
        database_event_listener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //gets list of users only once, for names. so doesn't fetch data on every nickname change
                val users_ref = FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(users_snapshot: DataSnapshot) {
                            local_data.clear()
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

                                /*
                                audio_namelist.add(song!!.getSoundName())
                                audio_urllist.add(song!!.getSoundUrl())
                                audio_artisitlist.add(song!!.getUploaderUserName())
                                audio_coverlist.add(song!!.getImageUrl())
                                audio_taglists.add(song!!.getTags())
                                 */
                            }
                            // fresh the datas to the datalist
                            if(is_query==false) {
                                refresh_data()
                            }
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
        querylist.clear()
        var min = local_data.size-datanumber
        if (min<0){
            min = 0
        }
        for (i in local_data.size-1 downTo min ){
            querylist.add(local_data[i])
        }
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item,querylist)
        discover_listview.adapter = soundbyteAdapter
    }

}
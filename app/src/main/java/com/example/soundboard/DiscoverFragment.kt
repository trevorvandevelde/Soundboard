package com.example.soundboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView


class DiscoverFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var discover_listview: ListView
    private lateinit var discover_search: SearchView
    private var datalist = ArrayList<SoundByteEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.discover_fragment, container, false)
        if(datalist.size == 0) {
            initData()
        }
        discover_search = view.findViewById(R.id.discover_search)
        discover_listview = view.findViewById(R.id.discover_listview)
        //discover_listview.setTextFilterEnabled(true)
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, datalist)
        discover_listview.adapter = soundbyteAdapter
        discover_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(requireActivity(), PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageUrl)
            intent.putExtra("title", soundbyte.title)
            intent.putExtra("audio", soundbyte.audioUrl)
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
                var querylist = ArrayList<SoundByteEntry>()
                var found:Boolean = false
                for(item in datalist) {
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
                if(query == ""){
                    soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, datalist)
                    discover_listview.adapter = soundbyteAdapter
                    return false
                }
                var querylist = ArrayList<SoundByteEntry>()
                var found:Boolean = false
                for(item in datalist) {
                    if (item.title.contains(query!!, ignoreCase = true)) {
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

    })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }


    // used to test
    private fun initData(){
        val tag_list:MutableList<String> = mutableListOf("DIY","SHIBUYA","AKIHABARA","GINZA")
        var time = 1
        repeat(10){
            datalist.add(SoundByteEntry("@ username", "NA",time.toString(), "12s",tag_list))
            time++
        }
    }

}
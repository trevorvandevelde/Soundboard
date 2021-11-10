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
        discover_listview.setTextFilterEnabled(true)
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, datalist)
        discover_listview.adapter = soundbyteAdapter
        discover_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(requireActivity(), PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageUrl)
            intent.putExtra("title", soundbyte.title)
            startActivity(intent)
        }

        discover_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String): Boolean{
                soundbyteAdapter.filter.filter(query)
                discover_listview.setAdapter(soundbyteAdapter)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                soundbyteAdapter.filter.filter(p0)
                return false
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }


    private fun initData(){
        val tag_list:MutableList<String> = mutableListOf("DIY","SHIBUYA","AKIHABARA","GINZA")
        repeat(10){
            datalist.add(SoundByteEntry("@ username", "NA","soundbyte title", "12s",tag_list))
        }
    }

}
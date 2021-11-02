package com.example.soundboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

class DiscoverFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var main_listview: ListView
    private var datalist = ArrayList<SoundByteEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.discover_fragment, container, false)

        initData()
        main_listview = view.findViewById(R.id.discover_listview)
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item, datalist)
        main_listview.adapter = soundbyteAdapter
        main_listview.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val soundbyte = datalist[position]
            val intent = Intent(requireActivity(), PlayActivity::class.java)
            intent.putExtra("image", soundbyte.imageID)
            intent.putExtra("title", soundbyte.title)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }

    private fun initData(){
        repeat(10){
            datalist.add(SoundByteEntry( R.drawable.dartmouth,"Test"))
        }
    }

}
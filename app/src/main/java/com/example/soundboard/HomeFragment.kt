package com.example.soundboard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var launchButton : Button
    private lateinit var soundbyteAdapter: SoundbyteAdapter
    private lateinit var main_listview: ListView
    private var datalist = ArrayList<SoundByteEntry>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)

        launchButton = view.findViewById(R.id.launchButton)
        launchButton.setOnClickListener{
            val intent = Intent(context, UploadSoundByteActivity::class.java)
            startActivity(intent)
        }

        if(datalist.size == 0) {
            initData()
        }
        main_listview = view.findViewById(R.id.home_listview)
        soundbyteAdapter = SoundbyteAdapter(requireContext(), R.layout.soundbyte_item,datalist)
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
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun initData(){
        repeat(10){
            datalist.add(SoundByteEntry( R.drawable.dartmouth,"Test"))
        }
    }


}
package com.example.soundboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val datalist = ArrayList<BoardEntry>()
    private lateinit var discover_recyclerview : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        if(datalist.size == 0) {
            initData()
        }
        discover_recyclerview = view.findViewById(R.id.board_recyclerview)
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        discover_recyclerview.layoutManager = layoutManager
        val adapter = BoardAdapter(datalist)
        discover_recyclerview.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun initData(){
        repeat(10){
            datalist.add(BoardEntry( R.drawable.dartmouth,"Test Board", "Test text for test board!"))
        }
    }

}
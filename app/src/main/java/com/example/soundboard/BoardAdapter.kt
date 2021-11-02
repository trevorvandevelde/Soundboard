package com.example.soundboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(var data: List<BoardEntry>)
    : RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image:ImageView = view.findViewById(R.id.board_image)
        val title:TextView = view.findViewById(R.id.board_name)
        val intro:TextView = view.findViewById(R.id.board_intro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = data[position]
        holder.image.setImageResource(board.imageID)
        holder.title.text = board.title
        holder.intro.text = board.intro
    }

    override fun getItemCount() = data.size
}
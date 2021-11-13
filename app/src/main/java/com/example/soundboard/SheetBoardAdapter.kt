package com.example.soundboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SheetBoardAdapter(var data: List<BoardEntry>)
    : RecyclerView.Adapter<SheetBoardAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image:ImageView = view.findViewById(R.id.board_image)
        val title:TextView = view.findViewById(R.id.board_name)
        val intro:TextView = view.findViewById(R.id.board_intro)
        val cardview: CardView = view.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sheet_board_item, parent, false)
        val viewholder = ViewHolder(view)
        viewholder.itemView.setOnClickListener {
            val position = viewholder.adapterPosition
            val board = data[position]

            val intent = Intent(view.getContext(), BoardActivity::class.java)
            intent.putExtra("image", board.imageUrl)
            intent.putExtra("title", board.title)
            view.getContext().startActivity(intent)
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = data[position]
        if(board.imageUrl != "NA") {
            Picasso.get().load(board.imageUrl).into(holder.image)
        }
        holder.title.text = board.title
        holder.intro.text = board.intro
    }

    override fun getItemCount() = data.size
}
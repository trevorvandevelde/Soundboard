package com.example.soundboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BoardAdapter(var data: List<BoardEntry>)
    : RecyclerView.Adapter<BoardAdapter.ViewHolder>(){

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image:ImageView = view.findViewById(R.id.board_image)
        val title:TextView = view.findViewById(R.id.board_name)
        val intro:TextView = view.findViewById(R.id.board_intro)
        val cardview: CardView = view.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.board_item, parent, false)
        val viewholder = ViewHolder(view)
        viewholder.itemView.setOnClickListener {
            val position = viewholder.adapterPosition
            val board = data[position]
            val intent = Intent(view.getContext(), BoardActivity::class.java)
            intent.putExtra("image", board.imageUrl)
            intent.putExtra("title", board.title)
            intent.putExtra("position", position.toString())
            view.getContext().startActivity(intent)
        }

        viewholder.itemView.setOnLongClickListener{
            val position = viewholder.adapterPosition
            val board = data[position]
            Delete_Board_Dialog().show((view.context as AppCompatActivity).getSupportFragmentManager(),"Delete_Board_Dialog")

            println(position)
            val deleteBoardDialog = Delete_Board_Dialog()
            val bundleBoard = Bundle()
            bundleBoard.putString("board_position", position.toString())
            deleteBoardDialog.arguments = bundleBoard
            deleteBoardDialog.show((view.context as AppCompatActivity).getSupportFragmentManager(),"Delete_Board_Dialog")
            true
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
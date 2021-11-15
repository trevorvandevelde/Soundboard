package com.example.soundboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ProfileImageAdapter(var data: ArrayList<Int>)
    : RecyclerView.Adapter<ProfileImageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sheet_board_item, parent, false)
        val viewholder =  ViewHolder(view)

        viewholder.itemView.setOnClickListener {
            val position = viewholder.adapterPosition
            
            Toast.makeText(view.context, "Successfully choose image ${position}!", Toast.LENGTH_SHORT)
                .show()
        }

        return viewholder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image_choose = data[position]
        holder.image.setImageResource(image_choose)
    }

    override fun getItemCount() = data.size

}
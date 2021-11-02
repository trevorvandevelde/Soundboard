package com.example.soundboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SoundbyteAdapter(context: Context, val resourceID: Int,
                       var data: List<SoundByteEntry>)
    : ArrayAdapter<SoundByteEntry>(context, resourceID, data) {

    inner class ViewHolder(val image: ImageView, val title: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
             val view:View
             val viewHolder: ViewHolder
             if(convertView == null) {
                 view = LayoutInflater.from(context).inflate(resourceID, parent, false)
                 val image = view.findViewById<ImageView>(R.id.soundbyte_image)
                 val name = view.findViewById<TextView>(R.id.soundbyte_title)
                 viewHolder = ViewHolder(image, name)
                 view.tag = viewHolder
             }
             else{
                 view = convertView
                 viewHolder = view.tag as ViewHolder
             }

             val SoundByteEntry = getItem(position)
             if(SoundByteEntry!=null){
                 viewHolder.image.setImageResource(SoundByteEntry.imageID)
                 viewHolder.title.text = SoundByteEntry.title
             }
             return view
         }
}
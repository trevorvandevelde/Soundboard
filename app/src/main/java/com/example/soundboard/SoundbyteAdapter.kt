package com.example.soundboard

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import co.lujun.androidtagview.ColorFactory
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import co.lujun.androidtagview.TagView.OnTagClickListener

class SoundbyteAdapter(context: Context, val resourceID: Int,
                       var data: List<SoundByteEntry>)
    : ArrayAdapter<SoundByteEntry>(context, resourceID, data) {

    inner class ViewHolder(val image: ImageView, val title: TextView,
                           val time: TextView, val author:TextView, val tag_container:TagContainerLayout )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
             val view:View
             val viewHolder: ViewHolder
             if(convertView == null) {
                 view = LayoutInflater.from(context).inflate(resourceID, parent, false)
                 val image = view.findViewById<ImageView>(R.id.soundbyte_image)
                 val title = view.findViewById<TextView>(R.id.soundbyte_title)
                 val time = view.findViewById<TextView>(R.id.soundbyte_time)
                 val author = view.findViewById<TextView>(R.id.soundbyte_author)
                 val tag_container:TagContainerLayout = view.findViewById(R.id.soundbyte_tagContainer)
                 tag_container.backgroundColor= Color. TRANSPARENT
                 tag_container.borderColor = Color.TRANSPARENT
                 tag_container.tagBackgroundColor = Color.rgb(245,245,245)
                 tag_container.tagBorderColor = Color.TRANSPARENT
                 viewHolder = ViewHolder(image, title, time, author, tag_container)
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
                 viewHolder.author.text = SoundByteEntry.author
                 viewHolder.time.text = SoundByteEntry.time
                 viewHolder.tag_container.removeAllTags();
                 for(item in SoundByteEntry.tag_list) {
                     viewHolder.tag_container.addTag(item)
                 }
             }
             return view
         }
}
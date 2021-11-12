package com.example.soundboard;

class SoundByteEntry(
        val id: String = "NA",
        val author: String = "@ username",
        //val imageID: Int = 0,
        val imageUrl: String = "NA",
        val title: String = "NA",
        val time: String = "NA",
        val tag_list:MutableList<String> =  mutableListOf("DIY"),
        val audioUrl:String = "NA",
)

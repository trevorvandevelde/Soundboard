package com.example.soundboard;

class SoundByteEntry(
        val author: String = "@ username",
        //val imageID: Int = 0,
        val imageUrl: String = "NA",
        val title: String = "soundbyte title",
        val time: String = "12s",
        val tag_list:MutableList<String> =  mutableListOf("DIY"),
        val audioUrl:String = "NA"
)

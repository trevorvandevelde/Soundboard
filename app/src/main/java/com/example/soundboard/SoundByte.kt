package com.example.soundboard

class SoundByte {

    private lateinit var songName: String
    private lateinit var songUrl: String
    private lateinit var imageUrl: String
    private lateinit var uploaderUserName : String
    private lateinit var id: String
    private lateinit var description: String
    private lateinit var tag: String



    fun SoundByte(songName : String, songUrl : String, uploaderUserName: String){
        this.songName = songName
        this.songUrl = songUrl
        //this.imageUrl = imageUrl
        this.uploaderUserName = uploaderUserName
    }
}
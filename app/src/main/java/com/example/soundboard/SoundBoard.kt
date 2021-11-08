package com.example.soundboard

class SoundBoard {

    private lateinit var soundBoardName : String
    private lateinit var listOfSounds : MutableList<SoundByte>
    private lateinit var userID : String

    fun SoundBoard(soundBoardName : String, userID: String){
        this.soundBoardName = soundBoardName
        this.userID = userID
        listOfSounds = arrayListOf()
    }

    fun addSoundToBoard(soundByte: SoundByte){
        listOfSounds.add(soundByte)
    }
}
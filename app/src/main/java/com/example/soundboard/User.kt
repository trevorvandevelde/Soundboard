package com.example.soundboard

class User {

    private lateinit var userID : String
    private lateinit var userDescription : String
    private lateinit var soundBoardsList : MutableList<SoundBoard>

    fun User(userID : String) {
        this.userID = userID
        userDescription = "Hi this is where you edit a description"
        soundBoardsList = arrayListOf()
    }

    fun getUserDescription() : String {
        return userDescription
    }

    fun setUserDescription(newDescription : String) {
        userDescription = newDescription
    }

    fun getUserID() : String {
        return userID
    }

    fun addSoundBoard(newSoundBoard : SoundBoard) {
        soundBoardsList.add(newSoundBoard)
    }

    fun getSoundBoards() : MutableList<SoundBoard>{
        return soundBoardsList
    }

    fun deleteSoundBoard(soundBoardToDelete : SoundBoard) {
        soundBoardsList.remove(soundBoardToDelete)
    }


}

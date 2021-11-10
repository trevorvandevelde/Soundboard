package com.example.soundboard

class User {

    //note: storing user with key as userid in firebase,
    //FirebaseDatabase.getInstance().getReference() -> snapshot.child("Users").child(<userId>).getValue(User::class.java)
    private lateinit var userNickname : String
    private lateinit var userDescription : String
    private lateinit var soundBoardsList : MutableList<SoundBoard>

    fun User(userNickname : String) {
        this.userNickname = userNickname
        userDescription = "Hi this is where you edit a description"
        soundBoardsList = arrayListOf()
    }


    fun getUserNickname() : String {
        return userNickname
    }

    fun setUserNickname(newNickname : String) {
        userNickname= newNickname
    }

    fun getUserDescription() : String {
        return userDescription
    }

    fun setUserDescription(newDescription : String) {
        userDescription = newDescription
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

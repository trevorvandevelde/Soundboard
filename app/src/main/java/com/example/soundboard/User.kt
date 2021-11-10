package com.example.soundboard

class User {

    //note: storing user with key as userid in firebase,
    //FirebaseDatabase.getInstance().getReference() -> snapshot.child("Users").child(<userId>).getValue(User::class.java)
    private lateinit var userNickname : String
    private lateinit var userDescription : String
    private lateinit var soundBoardsList : MutableList<SoundBoard>
    private lateinit var userSounds : MutableList<SoundByte>


    fun User(userNickname : String) {
        this.userNickname = userNickname
        userDescription = "Welcome! You can change your description in settings."
        soundBoardsList = arrayListOf()
        userSounds = arrayListOf()
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


    fun addSoundByteToUser(newSoundByte : SoundByte) {
        userSounds.add(newSoundByte)
    }

    fun getSoundBytes() : MutableList<SoundByte>{
        return userSounds
    }

    fun deleteSoundByte(soundByteToDelete : SoundByte) {
        userSounds.remove(soundByteToDelete)
    }


}

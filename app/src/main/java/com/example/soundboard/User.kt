package com.example.soundboard

class User {

    //note: storing user with key as userid in firebase,
    //FirebaseDatabase.getInstance().getReference() -> snapshot.child("Users").child(<userId>).getValue(User::class.java)
    private lateinit var userNickname : String
    private lateinit var userDescription : String
    private lateinit var soundBoardList : MutableList<SoundBoard>
    //private lateinit var userSounds : MutableList<Strings>


    fun User(userNickname : String) {
        this.userNickname = userNickname
        userDescription = "Welcome! Tap your profile image, nickname, or description to edit them."
        soundBoardList = arrayListOf()
        val sb : SoundBoard =  SoundBoard()
        sb.SoundBoard("My sounds")
        addSoundBoard(sb)
        //userSounds = arrayListOf()
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
        soundBoardList.add(newSoundBoard)
    }

    fun setSoundBoardList(soundboards: MutableList<SoundBoard>){
        soundBoardList = soundboards
    }

    fun getSoundBoardList() : MutableList<SoundBoard>{
        return soundBoardList
    }

    fun deleteSoundBoard(soundBoardToDelete : SoundBoard) {
        soundBoardList.remove(soundBoardToDelete)
    }

/*
    fun addSoundByteToUser(newSoundByte : SoundByte) {
        userSounds.add(newSoundByte)
    }

    fun getSoundBytes() : MutableList<SoundByte>{
        return userSounds
    }

    fun deleteSoundByte(soundByteToDelete : SoundByte) {
        userSounds.remove(soundByteToDelete)
    }
*/

}

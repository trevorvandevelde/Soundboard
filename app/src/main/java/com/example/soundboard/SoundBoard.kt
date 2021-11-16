package com.example.soundboard

// interface of the soundboard
class SoundBoard {

    private var soundBoardName : String = "New Board"
    private var soundByteIdMap : HashMap<String, Boolean> = HashMap()

    fun SoundBoard(soundBoardName : String){
        this.soundBoardName = soundBoardName
    }

    fun addSoundToBoard(id: String){
        soundByteIdMap.put(id, true)
    }


    fun getSoundBoardName() : String {
        return soundBoardName
    }

    fun setSoundBoardName(newName : String) {
        soundBoardName = newName
    }

    fun getSoundByteIdMap() : HashMap<String, Boolean> {
        return soundByteIdMap
    }

    fun removeSoundByteIdMap(idToRemove : String) {
        soundByteIdMap.remove(idToRemove)
    }
}
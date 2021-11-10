package com.example.soundboard

class SoundByte {

    private lateinit var soundName: String
    private lateinit var soundUrl: String
<<<<<<< HEAD
    private lateinit var imageUrl: String
    private lateinit var uploaderUserName: String
    private lateinit var id: String
    private lateinit var description: String
    private lateinit var tags: MutableList<String>


    fun SoundByte(soundName: String, imageUrl: String, soundUrl: String, uploaderUserName: String, description: String, tags: MutableList<String>) {
        this.soundName = soundName
        this.soundUrl = soundUrl
        this.imageUrl = imageUrl
=======
    //private lateinit var imageUrl: String
    private lateinit var uploaderUserName: String
    private lateinit var id: String
    private lateinit var description: String
    private lateinit var tags: Array<String>


    fun SoundByte(soundName: String, soundUrl: String, uploaderUserName: String, description: String, tags: Array<String>) {
        this.soundName = soundName
        this.soundUrl = soundUrl
        //this.imageUrl = imageUrl
>>>>>>> ca732aa (updated layout, added tags)
        this.uploaderUserName = uploaderUserName
        this.description = description
        this.tags = tags

<<<<<<< HEAD
=======

>>>>>>> ca732aa (updated layout, added tags)
    }

    fun getSoundDescription(): String {
        return description
    }

    fun setSoundDescription(newDescription : String){
        this.description = newDescription
    }

<<<<<<< HEAD
    fun getTags(): MutableList<String> {
        return tags
    }

    fun setTags(newTags : MutableList<String>) {
        this.tags = newTags
    }

    fun getSoundName(): String {
        return soundName
    }

    fun setSongName(soundName: String) {
        this.soundName = soundName
    }

    fun getSoundUrl(): String {
        return soundUrl
    }

    fun setSoundUrl(soundUrl : String){
        this.soundUrl = soundUrl
    }

    fun getImageUrl(): String {
       return imageUrl
=======
    fun getTags(): Array<String> {
        return tags
    }

    fun setTags(newTags : Array<String>) {
        this.tags = newTags
    }

    fun getSoundName(): String {
        return soundName
    }

    fun setSongName(soundName: String) {
        this.soundName = soundName
    }

    fun getSoundUrl(): String {
        return soundUrl
    }

    fun setSoundUrl(soundUrl : String){
        this.soundUrl = soundUrl
>>>>>>> ca732aa (updated layout, added tags)
    }

    //fun getImageUrl(): String {
       // return imageUrl
    //}

    fun setImageUrl(imageUrl : String){
        //this.imageUrl = imageUrl
    }

    fun getUploaderUserName() : String {
        return uploaderUserName
    }

    fun setUploaderUserName(uploaderUserName: String){
        this.uploaderUserName = uploaderUserName
    }
}
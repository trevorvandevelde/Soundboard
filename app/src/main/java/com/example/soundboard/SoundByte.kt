package com.example.soundboard

// interface of the soundbyte
class SoundByte {

    private lateinit var soundName: String
    private lateinit var soundUrl: String
    private lateinit var imageUrl: String
    private lateinit var uploaderUserName: String
    private lateinit var id: String
    private lateinit var description: String
    private lateinit var tags: MutableList<String>
    private lateinit var durationSeconds: String


    fun SoundByte(
        soundName: String,
        imageUrl: String,
        soundUrl: String,
        uploaderUserName: String,
        description: String,
        tags: MutableList<String>,
        durationSeconds: String
    ) {
        this.soundName = soundName
        this.soundUrl = soundUrl
        this.imageUrl = imageUrl
        this.uploaderUserName = uploaderUserName
        this.description = description
        this.tags = tags
        this.durationSeconds = durationSeconds
    }

    fun getSoundDescription(): String {
        return description
    }

    fun setSoundDescription(newDescription: String) {
        this.description = newDescription
    }

    fun getTags(): MutableList<String> {
        return tags
    }

    fun setTags(newTags: MutableList<String>) {
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

    fun setSoundUrl(soundUrl: String) {
        this.soundUrl = soundUrl
    }

    fun getImageUrl(): String {
        return imageUrl
    }


        fun setImageUrl(imageUrl: String) {
            this.imageUrl = imageUrl
        }

        fun getUploaderUserName(): String {
            return uploaderUserName
        }

        fun setUploaderUserName(uploaderUserName: String) {
            this.uploaderUserName = uploaderUserName
        }

        fun getDuration(): String {
            return durationSeconds
        }

        fun setDuration(newDuration: String) {
            this.durationSeconds = newDuration
        }
    }

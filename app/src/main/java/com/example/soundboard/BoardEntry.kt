package com.example.soundboard

class BoardEntry(
    val imageUrl: String = "NA",
    val title: String = "Board name",
    val intro: String = "0 soundbytes",
    val soundByteIdMap : HashMap<String, Boolean>
)
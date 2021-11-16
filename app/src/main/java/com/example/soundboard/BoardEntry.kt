package com.example.soundboard

// single board data structure in boardadapter
class BoardEntry(
    val imageUrl: String = "NA",
    val title: String = "Board name",
    val intro: String = "0 soundbytes",
    val soundByteIdMap : HashMap<String, Boolean>
)
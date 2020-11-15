package com.kromer.voicenotes

data class VoiceNote(
    val filePath: String,
    var playbackPosition: Int = 0,
    var duration: Int = 0
)
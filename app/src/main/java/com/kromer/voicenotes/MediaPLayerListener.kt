package com.kromer.voicenotes

interface MediaPLayerListener {
    fun onBufferingUpdate(progress: Int)

    fun onCompletion()

    fun onPrepared()
}
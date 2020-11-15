package com.kromer.voicenotes

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer

object MediaPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private var mediaPLayerListener: MediaPLayerListener? = null

    fun start(url: String, mMediaPLayerListener: MediaPLayerListener) {
        this.mediaPLayerListener = mMediaPLayerListener
        mediaPlayer = MediaPlayer().apply {
            setOnBufferingUpdateListener { _, i ->
                mediaPLayerListener?.onBufferingUpdate(i)
            }

            setOnPreparedListener {
                mediaPlayer = it
                mediaPLayerListener?.onPrepared()
                // call onCompletion after start() to get onCompletion properly
                mediaPlayer?.setOnCompletionListener {
                    mediaPLayerListener?.onCompletion()
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                setAudioAttributes(audioAttributes)
            } else {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
            setDataSource(url)
            prepareAsync()
        }
    }

    /**
     * call stop to release
     */
    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPLayerListener = null
    }

    fun resume(playbackPosition: Int) {
        mediaPlayer?.seekTo(playbackPosition)
        mediaPlayer?.start()
    }

    /**
     * return playbackPosition when paused
     */
    fun pause(): Int {
        mediaPlayer?.pause()
        return mediaPlayer?.currentPosition!!
    }

    fun isPlaying(): Boolean = mediaPlayer != null && mediaPlayer?.isPlaying!!

    fun currentPosition(): Int = mediaPlayer?.currentPosition!!

    fun duration(): Int = mediaPlayer?.duration!!
}
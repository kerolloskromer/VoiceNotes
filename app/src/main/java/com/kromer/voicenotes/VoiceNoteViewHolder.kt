package com.kromer.voicenotes

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_voice_note.view.*

class VoiceNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: VoiceNote, adapter: VoiceNotesAdapter) {

        handleView(
            adapterPosition == adapter.lastPlayedPosition,
            itemView.seekBar,
            itemView.iv_start_stop,
            itemView.tv_time,
            adapter,
            item
        )

        itemView.iv_start_stop.setOnClickListener {
            if (adapterPosition == adapter.lastPlayedPosition) {
                if (MediaPlayerManager.isPlaying()) {
                    item.playbackPosition = MediaPlayerManager.pause()
                } else {
                    MediaPlayerManager.resume(item.playbackPosition)
                }
                adapter.notifyItemChanged(adapter.lastPlayedPosition)
            } else {
                if (adapter.lastPlayedPosition != -1) {
                    adapter.items[adapter.lastPlayedPosition].playbackPosition =
                        MediaPlayerManager.pause()
                    adapter.notifyItemChanged(adapter.lastPlayedPosition)
                }
                itemView.progressBar.show()
                itemView.iv_start_stop.hide()
                MediaPlayerManager.start(
                    item.filePath,
                    object : MediaPLayerListener {
                        override fun onBufferingUpdate(progress: Int) {
                        }

                        override fun onCompletion() {
                            MediaPlayerManager.stop()
                            adapter.items[adapter.lastPlayedPosition].playbackPosition = 0
                            adapter.notifyItemChanged(adapter.lastPlayedPosition)
                            adapter.lastPlayedPosition = -1
                        }

                        override fun onPrepared() {
                            itemView.progressBar.hide()
                            itemView.iv_start_stop.show()
                            adapter.lastPlayedPosition = adapterPosition
                            item.duration = MediaPlayerManager.duration()
                            MediaPlayerManager.resume(item.playbackPosition)
                            adapter.notifyItemChanged(adapter.lastPlayedPosition)
                        }
                    })
            }
        }
    }

    private fun handleView(
        isPlaying: Boolean,
        seekBar: SeekBar,
        controller: ImageView,
        timerView: TextView,
        adapter: VoiceNotesAdapter,
        item: VoiceNote
    ) {
        seekBar.max = item.duration
        seekBar.progress = item.playbackPosition
        if (isPlaying && MediaPlayerManager.isPlaying()) {
            handlePlayingView(seekBar, controller, timerView, adapter, item)
        } else {
            handleNonPlayingView(seekBar, controller, timerView, adapter, item)
        }
    }

    private fun handlePlayingView(
        seekBar: SeekBar,
        controller: ImageView,
        timerView: TextView,
        adapter: VoiceNotesAdapter,
        item: VoiceNote
    ) {
        seekBar.isEnabled = true
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    MediaPlayerManager.resume(progress)
                    updateTime(
                        timerView,
                        progress,
                        item.duration
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        controller.setImageResource(R.drawable.ic_baseline_pause_24)
        seekBarProgressUpdater(adapter, seekBar, timerView)
    }

    private fun handleNonPlayingView(
        seekBar: SeekBar,
        controller: ImageView,
        timerView: TextView,
        adapter: VoiceNotesAdapter,
        item: VoiceNote
    ) {
        seekBar.isEnabled = false
        controller.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        updateTime(
            timerView,
            item.playbackPosition,
            item.duration
        )
        if (adapter.runnable != null) adapter.handler.removeCallbacks(adapter.runnable!!)
    }

    private fun seekBarProgressUpdater(
        adapter: VoiceNotesAdapter,
        seekBar: SeekBar,
        timerView: TextView
    ) {
        // we need to check if mediaPlayer is still playing because we can come here from handler late event after mediaPlayer has stopped
        if (!MediaPlayerManager.isPlaying()) return
        seekBar.progress = MediaPlayerManager.currentPosition()
        updateTime(
            timerView,
            MediaPlayerManager.currentPosition(),
            MediaPlayerManager.duration()
        )
        adapter.runnable =
            Runnable { seekBarProgressUpdater(adapter, seekBar, timerView) }
        adapter.handler.postDelayed(adapter.runnable!!, 100)
    }

    private fun updateTime(textView: TextView, current: Int, total: Int) {
        textView.text =
            formatMilliseconds(current.toLong()) + " / " + formatMilliseconds(total.toLong())
    }
}
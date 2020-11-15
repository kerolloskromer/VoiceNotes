package com.kromer.voicenotes

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class VoiceNotesAdapter(
    val items: List<VoiceNote>
) : RecyclerView.Adapter<VoiceNoteViewHolder>() {

    var lastPlayedPosition = -1
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VoiceNoteViewHolder {
        val root =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_voice_note, parent, false)
        return VoiceNoteViewHolder(root)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VoiceNoteViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem, this)
    }
}

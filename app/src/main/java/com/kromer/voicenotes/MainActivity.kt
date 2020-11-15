package com.kromer.voicenotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView(getList())
    }

    private fun setupRecyclerView(items: List<VoiceNote>) {
        val adapter = VoiceNotesAdapter(items)
        recyclerView.adapter = adapter
    }

    private fun getList(): List<VoiceNote> {
        val list: ArrayList<VoiceNote> = ArrayList()
        for (i in 0..255) {
            list.add(VoiceNote("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"))
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerManager.stop()
    }
}
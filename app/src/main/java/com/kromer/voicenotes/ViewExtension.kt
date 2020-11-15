package com.kromer.voicenotes

import android.view.View
import androidx.constraintlayout.widget.Group

fun View.show(): View {
    this.visibility = View.VISIBLE
    if (this is Group) {
        this.requestLayout()
    }
    return this
}

fun View.hide(): View {
    this.visibility = View.GONE
    if (this is Group) {
        this.requestLayout()
    }
    return this
}
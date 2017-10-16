package de.reiss.bible2net.theword.util.extensions

import android.view.View

inline fun View.onClick(crossinline function: () -> Unit) {
    setOnClickListener {
        function()
    }
}

fun View.visibleElseGone(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

fun View.visibleElseInvisible(predicate: Boolean) {
    visibility = if (predicate) View.VISIBLE else View.GONE
}

fun View.goneElseVisible(predicate: Boolean) {
    visibility = if (predicate) View.GONE else View.VISIBLE
}

fun View.greyOut(greyedOut: Boolean) {
    this.alpha = if (greyedOut) 0.3f else 1.0f
}

package de.reiss.bible2net.theword2.util.extensions

import android.content.Context

fun Context.dipToPx(dip: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dip * scale + 0.5f).toInt() // 0.5f for rounding
}

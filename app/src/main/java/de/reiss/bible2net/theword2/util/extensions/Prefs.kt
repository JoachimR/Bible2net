package de.reiss.bible2net.theword2.util.extensions

import android.content.SharedPreferences

fun SharedPreferences.change(func: SharedPreferences.Editor.() -> Unit) {
    val editor = this.edit()
    editor.func()
    editor.apply()
}

fun SharedPreferences.Editor.set(pair: Pair<String, String?>) {
    putString(pair.first, pair.second)
}

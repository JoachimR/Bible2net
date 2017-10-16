package de.reiss.bible2net.theword.util.extensions

import android.app.Activity
import android.content.Context
import android.support.annotation.UiThread
import android.view.inputmethod.InputMethodManager


@UiThread
fun Activity.hideKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus = currentFocus
    if (currentFocus != null) {
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

package de.reiss.bible2net.theword2.util.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import de.reiss.bible2net.theword2.events.eventBus

fun Activity.registerToEventBus() {
    eventBus.register(this)
}

fun Activity.unregisterFromEventBus() {
    eventBus.unregister(this)
}

fun Fragment.registerToEventBus() {
    eventBus.register(this)
}

fun Fragment.unregisterFromEventBus() {
    eventBus.unregister(this)
}

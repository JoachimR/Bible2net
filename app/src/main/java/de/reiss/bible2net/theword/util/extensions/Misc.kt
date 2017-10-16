package de.reiss.bible2net.theword.util.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import de.reiss.bible2net.theword.events.eventBus

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
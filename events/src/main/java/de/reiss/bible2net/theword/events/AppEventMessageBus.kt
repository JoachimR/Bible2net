package de.reiss.bible2net.theword.events

import org.greenrobot.eventbus.EventBus

val eventBus: EventBus by lazy {
    EventBus.getDefault()
}

fun postMessageEvent(event: AppEventMessage) {
    eventBus.post(event)
}

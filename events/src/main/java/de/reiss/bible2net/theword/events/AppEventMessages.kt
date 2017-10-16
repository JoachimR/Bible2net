package de.reiss.bible2net.theword.events

import java.util.*

sealed class AppEventMessage

data class TwdDownloadRequested(val bible: String, val year: Calendar) : AppEventMessage()

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

class FontSizeChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()

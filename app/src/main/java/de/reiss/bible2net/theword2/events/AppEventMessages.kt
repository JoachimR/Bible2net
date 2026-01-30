package de.reiss.bible2net.theword2.events

import java.util.Calendar

sealed class AppEventMessage

data class TwdDownloadRequested(val bible: String, val year: Calendar) : AppEventMessage()

data class ViewPagerMoveRequest(val position: Int) : AppEventMessage()

class FontSizeChanged : AppEventMessage()

class DatabaseRefreshed : AppEventMessage()

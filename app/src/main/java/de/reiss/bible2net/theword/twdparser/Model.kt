package de.reiss.bible2net.theword.twdparser

import java.util.Date

data class TwdItem(
    val bible: String,
    val bibleName: String,
    val bibleLanguageCode: String,
    val date: Date,
    val title: String,
    val parol1: Parol,
    val parol2: Parol
)

data class Parol(
    val book: String = "",
    val chapter: String = "",
    val verse: String = "",
    val id: String = "",
    val intro: String = "",
    val text: String = "",
    val ref: String = ""
)

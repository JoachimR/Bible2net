package de.reiss.bible2net.theword2.util

object BibleServerUrlBuilder {

    private const val BASE_URL = "https://www.bibleserver.com"

    private val translationMap = mapOf(
        "HoffnungFuerAlle" to "HFA",
        "Schlachter2000" to "SLT",
        "NeueEvangelistischeUebersetzung" to "NeÜ",
        "EnglishStandardVersion" to "ESV",
        "LeonbergerBibel" to "LUT"
    )

    fun buildUrl(bible: String, ref: String): String {
        val translation = translationMap[bible] ?: bible.ifEmpty { "LUT" }
        return "$BASE_URL/$translation/$ref"
    }
}

package de.reiss.bible2net.theword.downloader.list

import com.squareup.moshi.Json
import java.util.*

data class Twd11(@field:Json(name = "category") val category: String,
                 @field:Json(name = "year") val year: Int,
                 @field:Json(name = "lang") val language: String,
                 @field:Json(name = "bible") val bible: String,
                 @field:Json(name = "biblename") val bibleName: String,
                 @field:Json(name = "updated") val lastUpdated: Date,
                 @field:Json(name = "url") val twdFileUrl: String)
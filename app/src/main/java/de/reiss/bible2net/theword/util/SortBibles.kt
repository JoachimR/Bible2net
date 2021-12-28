package de.reiss.bible2net.theword.util

import de.reiss.bible2net.theword.model.Bible

fun sortBibles(bibles: List<Bible>) = bibles.sortedWith(
    compareBy({ it.languageCode }, { it.bibleName })
)

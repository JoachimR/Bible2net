package de.reiss.bible2net.theword2.util

import de.reiss.bible2net.theword2.model.Bible

fun sortBibles(bibles: List<Bible>) = bibles.sortedWith(
    compareBy({ it.languageCode }, { it.bibleName })
)

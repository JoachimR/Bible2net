package de.reiss.bible2net.theword.bible.list

import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.view.StableListItem

object BibleListBuilder {

    fun buildList(bibles: List<Bible>): List<StableListItem> = bibles
            .sortedBy { it.languageCode }
            .map { BibleListItem(it) }

}

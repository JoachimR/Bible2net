package de.reiss.bible2net.theword2.bible.list

import de.reiss.bible2net.theword2.model.Bible
import de.reiss.bible2net.theword2.util.view.StableListItem

data class BibleListItem(val bible: Bible) : StableListItem() {

    override fun stableId() = hashCode().toLong()
}

package de.reiss.bible2net.theword.bible.list

import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.view.StableListItem

data class BibleListItem(val bible: Bible) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}

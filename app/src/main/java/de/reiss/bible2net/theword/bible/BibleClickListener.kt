package de.reiss.bible2net.theword.bible

import de.reiss.bible2net.theword.model.Bible

interface BibleClickListener {

    fun onBibleClicked(bible: Bible)
}

package de.reiss.bible2net.theword.note.list

import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.util.view.StableListItem

data class NoteListItem(val note: Note) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}

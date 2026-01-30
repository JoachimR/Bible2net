package de.reiss.bible2net.theword2.note.list

import de.reiss.bible2net.theword2.model.Note
import de.reiss.bible2net.theword2.util.view.StableListItem

data class NoteListItem(val note: Note) : StableListItem() {

    override fun stableId() = hashCode().toLong()
}

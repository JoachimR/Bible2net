package de.reiss.bible2net.theword2.note.list

import de.reiss.bible2net.theword2.model.Note
import de.reiss.bible2net.theword2.util.view.StableListItem

object NoteListBuilder {

    fun buildList(notes: List<Note>): List<StableListItem> = notes
        .sortedBy { it.date }
        .map { NoteListItem(it) }
}

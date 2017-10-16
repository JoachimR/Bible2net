package de.reiss.bible2net.theword.note.list

import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.util.view.StableListItem

object NoteListBuilder {

    fun buildList(notes: List<Note>): List<StableListItem> = notes
            .sortedBy { it.date }
            .map { NoteListItem(it) }

}
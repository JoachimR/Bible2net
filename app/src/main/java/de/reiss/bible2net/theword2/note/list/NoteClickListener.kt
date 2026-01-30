package de.reiss.bible2net.theword2.note.list

import de.reiss.bible2net.theword2.model.Note

interface NoteClickListener {

    fun onNoteClicked(note: Note)
}

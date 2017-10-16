package de.reiss.bible2net.theword.note.list

import de.reiss.bible2net.theword.model.Note

interface NoteClickListener {

    fun onNoteClicked(note: Note)

}
package de.reiss.bible2net.theword.note.list

import de.reiss.bible2net.theword.model.Note

data class FilteredNotes constructor(
    val allItems: List<Note>,
    val filteredItems: List<Note>,
    val query: String
) {

    constructor() : this(emptyList(), emptyList(), "")
}

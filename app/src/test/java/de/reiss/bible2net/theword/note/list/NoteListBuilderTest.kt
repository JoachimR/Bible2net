package de.reiss.bible2net.theword.note.list

import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.testutil.sampleNote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("IllegalIdentifier")
class NoteListBuilderTest {

    @Test
    fun `when no notes then empty list`() {
        assertEquals(0, NoteListBuilder.buildList(emptyList()).size)
    }

    @Test
    fun `when 1 note then list with 1 item`() {
        checkForSize(1)
    }

    @Test
    fun `when 999 note then list with 999 items`() {
        checkForSize(999)
    }

    private fun checkForSize(size: Int) {
        val modelList = mutableListOf<Note>()
        (1..size).mapTo(modelList) { sampleNote(it) }

        val listItems = NoteListBuilder.buildList(modelList)
        assertEquals(size, listItems.size)

        for ((index, listItem) in listItems.withIndex()) {
            assertTrue(listItem is NoteListItem)

            with(listItem as NoteListItem) {
                assertEquals(modelList[index].noteText, listItem.note.noteText)
            }
        }
    }
}

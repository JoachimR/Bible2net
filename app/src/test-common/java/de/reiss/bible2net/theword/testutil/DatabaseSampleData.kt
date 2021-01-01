package de.reiss.bible2net.theword.testutil

import de.reiss.bible2net.theword.database.BibleItem
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.TheWordItem

fun sampleNoteItem(number: Int) = NoteItem(
    dateForNumber(number),
    "#$number book1",
    "#$number chapter1",
    "#$number verse1",
    "#$number id1",
    "#$number intro1",
    "#$number text1",
    "#$number ref1",
    "#$number book2",
    "#$number chapter2",
    "#$number verse2",
    "#$number id2",
    "#$number intro2",
    "#$number text2",
    "#$number ref2",
    "#$number note"
)

fun sampleTheWordItem(number: Int, bibleId: Int) = TheWordItem(
    10 + number,
    bibleId,
    dateForNumber(number),
    "#$number book1",
    "#$number chapter1",
    "#$number verse1",
    "#$number id1",
    "#$number intro1",
    "#$number text1",
    "#$number ref1",
    "#$number book2",
    "#$number chapter2",
    "#$number verse2",
    "#$number id2",
    "#$number intro2",
    "#$number text2",
    "#$number ref2"
)

fun sampleBibleItem(bibleId: Int) = BibleItem("testBible", "test Bible", "tst")
    .apply { id = bibleId }

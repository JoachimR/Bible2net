package de.reiss.bible2net.theword.testutil

import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.model.TheWordContent

fun sampleNote(number: Int) = Note(
        date = dateForNumber(number),
        noteText = "#$number note",
        theWordContent = sampleTheWordContent(number))

fun sampleTheWord(number: Int, bible: String) = TheWord(
        bible = bible,
        date = dateForNumber(number),
        content = sampleTheWordContent(number))

fun sampleTheWordContent(number: Int): TheWordContent {
    return TheWordContent(
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
            "#$number ref2")
}

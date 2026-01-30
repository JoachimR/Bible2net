package de.reiss.bible2net.theword2.database.converter;


import de.reiss.bible2net.theword2.database.NoteItem;
import de.reiss.bible2net.theword2.database.TheWordItem;
import de.reiss.bible2net.theword2.model.Note;
import de.reiss.bible2net.theword2.model.TheWord;
import de.reiss.bible2net.theword2.model.TheWordContent;

public class Converter {

    public static TheWord theWordItemToTheWord(String bible, TheWordItem theWordItem) {
        if (theWordItem == null) {
            return null;
        }
        return new TheWord(
                bible, theWordItem.date, new TheWordContent(theWordItem.book1, theWordItem.chapter1,
                theWordItem.verse1, theWordItem.id1, theWordItem.intro1, theWordItem.text1,
                theWordItem.ref1, theWordItem.book2, theWordItem.chapter2, theWordItem.verse2,
                theWordItem.id2, theWordItem.intro2, theWordItem.text2, theWordItem.ref2));
    }

    public static Note noteItemToNote(NoteItem noteItem) {
        if (noteItem == null) {
            return null;
        }
        return new Note(noteItem.date, noteItem.note, new TheWordContent(
                noteItem.book1, noteItem.chapter1, noteItem.verse1, noteItem.id1, noteItem.intro1,
                noteItem.text1, noteItem.ref1, noteItem.book2, noteItem.chapter2, noteItem.verse2,
                noteItem.id2, noteItem.intro2, noteItem.text2, noteItem.ref2));
    }

}
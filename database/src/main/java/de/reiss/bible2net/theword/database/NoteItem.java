package de.reiss.bible2net.theword.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import de.reiss.bible2net.theword.model.TheWordContent;

@Entity(indices = {@Index(value = {"date"}, unique = true)})
public class NoteItem {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public Date date;
    public String book1;
    public String chapter1;
    public String verse1;
    public String id1;
    public String intro1;
    public String text1;
    public String ref1;
    public String book2;
    public String chapter2;
    public String verse2;
    public String id2;
    public String intro2;
    public String text2;
    public String ref2;
    public String note;

    public NoteItem() {
    }

    @Ignore
    public NoteItem(Date date, String book1, String chapter1, String verse1, String id1,
                    String intro1, String text1, String ref1, String book2, String chapter2,
                    String verse2, String id2, String intro2, String text2, String ref2, String note) {
        this.date = date;
        this.book1 = book1;
        this.chapter1 = chapter1;
        this.verse1 = verse1;
        this.id1 = id1;
        this.intro1 = intro1;
        this.text1 = text1;
        this.ref1 = ref1;
        this.book2 = book2;
        this.chapter2 = chapter2;
        this.verse2 = verse2;
        this.id2 = id2;
        this.intro2 = intro2;
        this.text2 = text2;
        this.ref2 = ref2;
        this.note = note;
    }

    @Ignore
    public NoteItem(Date date, TheWordContent content, String note) {
        this.date = date;
        this.book1 = content.getBook1();
        this.chapter1 = content.getChapter1();
        this.verse1 = content.getVerse1();
        this.id1 = content.getId1();
        this.intro1 = content.getIntro1();
        this.text1 = content.getText1();
        this.ref1 = content.getRef1();
        this.book2 = content.getBook2();
        this.chapter2 = content.getChapter2();
        this.verse2 = content.getVerse2();
        this.id2 = content.getId2();
        this.intro2 = content.getIntro2();
        this.text2 = content.getText2();
        this.ref2 = content.getRef2();
        this.note = note;
    }

}

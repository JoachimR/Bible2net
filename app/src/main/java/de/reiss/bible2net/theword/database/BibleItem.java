package de.reiss.bible2net.theword.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"bible"}, unique = true)})
public class BibleItem {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String bible;
    public String name;
    public String languageCode;

    public BibleItem() {
    }

    @Ignore
    public BibleItem(String bible, String name, String languageCode) {
        this.bible = bible;
        this.name = name;
        this.languageCode = languageCode;
    }

}

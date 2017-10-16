package de.reiss.bible2net.theword.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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

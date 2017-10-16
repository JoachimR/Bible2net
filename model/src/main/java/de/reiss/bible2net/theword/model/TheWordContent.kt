package de.reiss.bible2net.theword.model

import android.os.Parcel
import android.os.Parcelable

data class TheWordContent(val book1: String,
                          val chapter1: String,
                          val verse1: String,
                          val id1: String,
                          val intro1: String,
                          val text1: String,
                          val ref1: String,
                          val book2: String,
                          val chapter2: String,
                          val verse2: String,
                          val id2: String,
                          val intro2: String,
                          val text2: String,
                          val ref2: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(book1)
        writeString(chapter1)
        writeString(verse1)
        writeString(id1)
        writeString(intro1)
        writeString(text1)
        writeString(ref1)
        writeString(book2)
        writeString(chapter2)
        writeString(verse2)
        writeString(id2)
        writeString(intro2)
        writeString(text2)
        writeString(ref2)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TheWordContent> = object : Parcelable.Creator<TheWordContent> {
            override fun createFromParcel(source: Parcel): TheWordContent = TheWordContent(source)
            override fun newArray(size: Int): Array<TheWordContent?> = arrayOfNulls(size)
        }
    }
}
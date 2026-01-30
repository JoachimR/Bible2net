package de.reiss.bible2net.theword2.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Note(
    val date: Date,
    val noteText: String,
    val theWordContent: TheWordContent
) : Comparable<Note>, Parcelable {

    override fun compareTo(other: Note) = this.date.compareTo(other.date)

    constructor(source: Parcel) : this(
        source.readSerializable() as Date,
        source.readString()!!,
        source.readParcelable<TheWordContent>(TheWordContent::class.java.classLoader)!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeSerializable(date)
        writeString(noteText)
        writeParcelable(theWordContent, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Note> = object : Parcelable.Creator<Note> {
            override fun createFromParcel(source: Parcel): Note = Note(source)
            override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
        }
    }
}

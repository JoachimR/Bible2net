package de.reiss.bible2net.theword.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class TheWord(val bible: String,
                   val date: Date,
                   val content: TheWordContent) : Comparable<TheWord>, Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TheWord> = object : Parcelable.Creator<TheWord> {
            override fun createFromParcel(source: Parcel): TheWord = TheWord(source)
            override fun newArray(size: Int): Array<TheWord?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readSerializable() as Date,
            source.readParcelable<TheWordContent>(TheWordContent::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(bible)
        writeSerializable(date)
        writeParcelable(content, 0)
    }

    override fun compareTo(other: TheWord): Int = this.date.compareTo(other.date)

}
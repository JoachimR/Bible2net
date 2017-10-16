package de.reiss.bible2net.theword.model

import android.os.Parcel
import android.os.Parcelable

data class Bible(val key: String, val bibleName: String, val languageCode: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(key)
        writeString(bibleName)
        writeString(languageCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Bible> = object : Parcelable.Creator<Bible> {
            override fun createFromParcel(source: Parcel): Bible = Bible(source)
            override fun newArray(size: Int): Array<Bible?> = arrayOfNulls(size)
        }
    }

}
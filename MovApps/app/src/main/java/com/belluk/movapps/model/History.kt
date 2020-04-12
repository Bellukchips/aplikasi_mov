package com.belluk.movapps.model

import android.os.Parcel
import android.os.Parcelable
data class History(
    var id :Int = 0,
    var nama:String?="",
    var bioskop:String?="",
    var genre:String?="",
    var user:String?="",
    var date:String?="",
    var poster:String?="",
    var rating:String?="",
    var timer:String?="",
    var kodeTiket:String?="",
    var cinema:String?=""
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nama)
        parcel.writeString(bioskop)
        parcel.writeString(genre)
        parcel.writeString(user)
        parcel.writeString(date)
        parcel.writeString(poster)
        parcel.writeString(rating)
        parcel.writeString(timer)
        parcel.writeString(kodeTiket)
        parcel.writeString(kodeTiket)
        parcel.writeString(cinema)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }

}
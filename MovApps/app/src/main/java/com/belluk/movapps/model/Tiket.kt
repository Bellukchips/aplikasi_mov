package com.belluk.movapps.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tiket(
    var nama:String?="",
    var bioskop:String?="",
    var genre:String?="",
    var user:String?="",
    var date:String?="",
    var poster:String?="",
    var rating:String?="",
    var timer:String?="",
    var kodeTiket:String?="",
    var cinema:String?="",
    var dateBuy:String?=""
):Parcelable

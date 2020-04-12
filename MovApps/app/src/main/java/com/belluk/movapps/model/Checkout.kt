package com.belluk.movapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Checkout (
    var kursi: String ?="",
    var harga: String ?="",
    var status:String?="",
    var golSeat:String?="",
    var cinema:String?="",
    var bioskop:String?=""
): Parcelable
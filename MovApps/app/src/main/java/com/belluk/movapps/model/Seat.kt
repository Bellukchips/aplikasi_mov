package com.belluk.movapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Seat(var nama:String?="",var status:String?="",var checked:Boolean = false,var golSeat:String?=""):Parcelable
package com.belluk.movapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet(var user:String?="",val kode:String?="",var title:String?="",var date:String?="",var money:String?="",var status:String?=""):Parcelable
package com.example.panplaner

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Project(val uid: String, val creator: String, val name: String, val deadline: String, val users: String): Parcelable {
    constructor(): this("", "", "", "", "")

}
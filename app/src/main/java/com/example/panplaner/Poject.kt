package com.example.panplaner

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Array

@Parcelize
class Project(val uid: String, val creator: String, val name: String, val deadline: String, val users: HashMap<String, String>?): Parcelable {
    constructor(): this("", "", "", "", HashMap<String, String>())

}
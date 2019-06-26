package com.example.panplaner

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.project_list_item.view.*

class Project(val uid: String, val creator: String, val name: String,val deadline: String, val users: String): Parcelable, Item<ViewHolder>() {
    constructor(): this("", "", "", "", "")

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView2.text = name
    }

    override fun getLayout(): Int {
        return R.layout.project_list_item
    }

    override fun writeToParcel(parcel: Parcel, flags: Int){
        parcel.writeString(uid)
        parcel.writeString(creator)
        parcel.writeString(name)
        parcel.writeString(deadline)
        parcel.writeString(users)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(p0: Parcel?): Project {
            Log.d("creator project", "$p0")
            return Project()
        }

        override fun newArray(p0: Int): Array<Project?> {
            return arrayOfNulls(p0)
        }
    }
}
package com.example.panplaner

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.xwray.groupie.Item
import com.example.panplaner.Project
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.project_list_item.view.*

class ProjectItem(val project: Project?): Item<ViewHolder>(), Parcelable {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView2.text = project?.name
    }

    override fun getLayout(): Int {
        return R.layout.project_list_item
    }

    override fun writeToParcel(parcel: Parcel, flags: Int){
        parcel.writeString(project?.uid)
        parcel.writeString(project?.creator)
        parcel.writeString(project?.name)
        parcel.writeString(project?.deadline)

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
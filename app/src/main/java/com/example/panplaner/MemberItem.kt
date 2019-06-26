package com.example.panplaner

import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.member_listitem.view.*


class MemberItem(val user: Member): Item<ViewHolder>() {
    override fun getLayout(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewmember.text = user?.name
    }
}
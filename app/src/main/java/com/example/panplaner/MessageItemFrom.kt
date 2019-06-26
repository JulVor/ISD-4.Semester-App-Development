package com.example.panplaner

import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_message_row.view.*

class MessageItemFrom(val message: Message?): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = message?.message
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_row
    }
}
package com.example.panplaner

import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_message_row.view.*
import kotlinx.android.synthetic.main.chat_message_to_row.view.*

class MessageItem(val message: Message?): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = message?.message
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_row
    }
}
package com.example.panplaner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatItem())
        adapter.add(ChatItem())
        adapter.add(ChatItem())
        adapter.add(ChatItem())

        recyclerview_chat.adapter= adapter
    }
}

class ChatItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_row
    }
}
package com.example.panplaner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_create_project.*
import java.util.*
import kotlinx.android.synthetic.main.chat_message_row.view.*
import kotlinx.android.synthetic.main.chat_message_to_row.view.*

class ChatActivity : AppCompatActivity() {
    val frag = "ChatActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setupDummyData()
    }
    private fun setupDummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatToItem())

        recyclerview_chat.adapter= adapter
        send_button_chat.setOnClickListener {
            saveMessageToDatabase()
        }
    }




    private fun saveMessageToDatabase() {
        val user = FirebaseAuth.getInstance().uid ?: ""
        val projectID = intent.getStringExtra("projectID")
        val uid = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Messages/$projectID/$uid")
        val message = Message(projectID, user, messageChat.text.toString())
        ref.setValue(message)
            .addOnSuccessListener {
                Log.d(frag, "message send")
            }
    }

    private fun getMessages(){
        val user = FirebaseAuth.getInstance().uid ?: ""
        recyclerview_chat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            val adapter = GroupAdapter<ViewHolder>()
            adapter.apply{
                
            }

        }
    }

}

class ChatFromItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = "from Message"
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_row
    }
}

class ChatToItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = "to Message"
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_to_row
    }
}
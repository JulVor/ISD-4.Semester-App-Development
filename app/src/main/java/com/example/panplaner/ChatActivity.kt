package com.example.panplaner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_create_project.*
import java.util.*
import kotlinx.android.synthetic.main.chat_message_row.view.*
import kotlinx.android.synthetic.main.chat_message_to_row.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class ChatActivity : AppCompatActivity() {

    companion object {
        val TAG = "MessageLog"
    }
    val adapter = GroupAdapter<ViewHolder>()
    val frag = "ChatActivity"
    var projectID: String = ""
    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val projectName = intent.getStringExtra("projectName")
        supportActionBar?.title = projectName
        getName()
        projectID = intent.getStringExtra("projectID")
        listenForMessages()
        send_button_chat.setOnClickListener {
            saveMessageToDatabase()
        }
    }

    private fun getName(){
        val userID = FirebaseAuth.getInstance().uid  ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userID/username")
        Log.d(frag, ref.toString())
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                username = p0.value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun saveMessageToDatabase() {
        val user = FirebaseAuth.getInstance().uid ?: ""
        val username = username
        Log.d(frag, "sending")
        Log.d(frag, projectID)
        val ref = FirebaseDatabase.getInstance().getReference("/Messages/$projectID").push()
        Log.d(frag, ref.toString())
        if(messageChat.text.toString() != "") {
            val message = Message(projectID, user, username, messageChat.text.toString(), System.currentTimeMillis() / 1000)
            ref.setValue(message)
                .addOnSuccessListener {
                    Log.d(frag, "message send")
                    messageChat.text.clear()
                }
        }

    }

    private fun listenForMessages(){
            val projectID = intent.getStringExtra("projectID")
            val user = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/Messages").child(projectID)
            adapter.apply {
                ref.addChildEventListener(object : ChildEventListener {

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val message = p0.getValue(Message::class.java)
                        if(message != null){
                            Log.d(TAG, message?.message.toString())
                            if(message.sendFromId == FirebaseAuth.getInstance().uid){
                                adapter.add(ChatToItem(message))
                            } else {
                                adapter.add(ChatFromItem(message))
                            }
                        }
                        recyclerview_chat.adapter = adapter
                    }
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                    override fun onChildRemoved(p0: DataSnapshot) {}
                })
            }
        }


}

class ChatFromItem(val message: Message?): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = message?.message.toString()
        viewHolder.itemView.textView_from_row_user.text = message?.sendFromName.toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_row
    }
}

class ChatToItem(val message: Message?): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = message?.message.toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_to_row
    }
}
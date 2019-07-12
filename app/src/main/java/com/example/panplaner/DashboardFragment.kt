package com.example.panplaner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.deadline_list_item.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.example.panplaner.LogInFragment.OnFragmentInteractionListener
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class DashboardFragment(pid: String, creator: String) : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var projectUid: String? = pid
    val frag = "DashboardFragment"
    private var name: String? = ""
    private var creatorProject: String? = creator
    private var userExists: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(frag, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(frag, "onCreate")
        val auth = FirebaseAuth.getInstance().uid
        Log.d(frag, projectUid)

        Log.d(frag, creatorProject)
        //var data = getProject()
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddUser.setOnClickListener {
            Log.d(frag, "butotn clicked")
            val usr = editTextAddUser.text.toString()
            if(usr != null){
                GlobalScope.launch(Dispatchers.Main){
                    checkIfUserExists(usr)
                    editTextAddUser.text.clear()
                }
            }
            else{
                Toast.makeText(activity, "Enter an Username", Toast.LENGTH_SHORT).show()
            }
        }
        button_chat.setOnClickListener {
            val intent = Intent(context, ChatActivity()::class.java)
            Log.d(frag, projectUid)
            intent.putExtra("projectID", projectUid)
            intent.putExtra("projectName", name)
            startActivity(intent)
        }
    }

    suspend fun checkIfUserExists(user: String){
        Log.d(frag, "checking if $user exists................................")
        Log.d(frag, user)
        val uRef = FirebaseDatabase.getInstance().getReference("/users")
        var returnVal = false
        uRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d(frag, it.child("username").value.toString())
                    if(user == it.child("username").value.toString()){
                        Log.d(frag, "its $user")
                        returnVal = true
                        Log.d(frag, returnVal.toString())
                        val refP = FirebaseDatabase.getInstance().getReference("/Projects/$creatorProject/$projectUid/users").push()
                        refP.setValue(user).addOnSuccessListener { Log.d(frag, "$user was added to project $projectUid") }
                        val refU = FirebaseDatabase.getInstance().getReference("/users/${it.child("uid").value}/projects").push()
                        refU.setValue(projectUid).addOnSuccessListener { Log.d(frag, "$projectUid was added to $user 's projects") }
                    }else{
                        Log.d(frag, "its not $user")
                    }
                }
            }
        })
    }

    suspend fun addUserToProject(user: String) {
            Log.d(frag, "addtouser true")
            val refP = FirebaseDatabase.getInstance().getReference("/Projects/$creatorProject/$projectUid/users").push()
            refP.setValue(user).addOnSuccessListener { Log.d(frag, "$user was added to project $projectUid") }
    }

    private fun findUser(user: String) {
        val refU = FirebaseDatabase.getInstance().getReference("/users/")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun getProject() {
        Log.d(frag, "getProject")
        var ref = FirebaseDatabase.getInstance().getReference("/Projects").child("${FirebaseAuth.getInstance().uid}").child(projectUid.toString()).child("users")
        Log.d(frag, "$ref")
        recyclerView.apply {
            val adapter = GroupAdapter<ViewHolder>()
            adapter.apply {
                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        p0.children.forEach {
                            Log.d(frag, "getting data..")
                            Log.d(frag, "${it.value}")
                            val member = it.getValue()
                            Log.d(frag, member.toString())
                        }
                        recyclerView.adapter = adapter
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Log.w(frag, "something went wrong...")
                    }
                })
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
class Deadline(val datum: String, val belongs_to: String, val description: String){
    constructor(): this("", "", "")
}

class DeadlineItem(val deadline: Deadline?): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.deadline_date.text = deadline?.datum
        viewHolder.itemView.deadline_user.text = deadline?.belongs_to
    }

    override fun getLayout(): Int {
        return R.layout.deadline_list_item
    }
}


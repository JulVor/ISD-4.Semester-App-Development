package com.example.panplaner

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.example.panplaner.LogInFragment.OnFragmentInteractionListener
import com.google.firebase.database.DatabaseError


class DashboardFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    val frag = "DashboardFragment"
    private var name: String? = ""
    private var projectUid: String? = ""
    private var mproject: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        name = arguments?.getString("name")
        projectUid = arguments?.getString("uid")
        Log.d(frag, "$name")
        val auth = FirebaseAuth.getInstance().uid
        Log.d(frag, auth)
        Log.d(frag, projectUid)
        var data = getProject()
        Log.d(frag, data.toString()
        )
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_chat.setOnClickListener {
            val intent = Intent(context, ChatActivity()::class.java)
            startActivity(intent);
        }
        super.onViewCreated(view, savedInstanceState)
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


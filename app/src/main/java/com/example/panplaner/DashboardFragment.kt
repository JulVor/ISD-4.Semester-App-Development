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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DashboardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class DashboardFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "DashboardFragment"
    private var name: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        name = arguments?.getString("name")
        Log.d(frag, "$name")

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

    private fun getProject(position: Int) {

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


package com.example.panplaner

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_project.*
import java.util.*
import kotlin.collections.HashMap

class createProjectFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "createProjectFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonCreateProject.setOnClickListener {
            if (editTextProjectName.text.isNotEmpty() && editDeadline.text.isNotEmpty()) {
                saveProjectToDatabase()
                activity?.supportFragmentManager?.popBackStackImmediate()
            } else{
                Toast.makeText(activity, "Enter a name and a deadline!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveProjectToDatabase() {
        val user = FirebaseAuth.getInstance().uid ?: ""
        val uID = UUID.randomUUID().toString()
        val refP = FirebaseDatabase.getInstance().getReference("/Projects/$user/$uID")
        val refU = FirebaseDatabase.getInstance().getReference("/users/$user/projects").push()
        val hashmap = HashMap<String, String>()
        hashmap.put("member", editTextUser.text.toString())
        val project = Project(uID, user, editTextProjectName.text.toString(), editDeadline.text.toString(), hashmap)
        refP.setValue(project)
            .addOnSuccessListener {
                Log.d(frag, "project created")
            }
        refU.setValue(uID)
            .addOnSuccessListener {
                Log.d(frag, "project $uID pushed to $user projetcs.")
            }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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

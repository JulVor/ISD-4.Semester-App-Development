package com.example.panplaner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "LogInFragment"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(frag, "onCreateView")
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        Log.d(frag, "onAttach")
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(frag, "onActivityCreated")
        textViewLogIn.setOnClickListener {
            Log.d(frag, "text clicked")
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            findNavController().navigate(action)

        }
        buttonLogIn.setOnClickListener{
            Log.d(frag, "button clicked")
            val email = editTextEmailLogIn.text.toString()
            val password = editTextPasswordLogIn.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(activity, "Enter Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d(frag, "success")
                        val user = auth.currentUser
                        Log.d(frag, user?.uid)
                        val intent = Intent(activity, UserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Log.w(frag, "failure")
                    }
                }
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        Log.d(frag, "onDetach")
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

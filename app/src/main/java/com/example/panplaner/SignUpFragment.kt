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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "SignUpFragment"
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

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(frag, "onActivityCreated")
        buttonSubmitSignUp.setOnClickListener {
            Log.d(frag, "Button clicked")
            val email = editTextEmailSignUp.text.toString()
            val password = editTextPasswordSignUp.text.toString()
            val username = editTextUsernameSignUp.text.toString()
            if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(activity, "Enter Username, Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(frag, "createUserWithEmail: success")
                        val user = auth.currentUser
                        Log.d(frag, user.toString())
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                    } else {
                        Log.w(frag, "createUserWithEmail: failure", task.exception)
                    }
                }

        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewAlreadySignedUpSignUp.setOnClickListener {
            Log.d(frag, "text clicked")
            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
            findNavController().navigate(action)
        }
        buttonSubmitSignUp.setOnClickListener {
            val email = editTextEmailSignUp.text.toString()
            if(email.isEmpty()){
                editTextEmailSignUp.error = "Could not find email."
            } else {
                val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
                action.arguments.get(email)
                findNavController().navigate(action)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d(frag, currentUser.toString())
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        Log.d(frag, "onAttach")
        super.onAttach(context)
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

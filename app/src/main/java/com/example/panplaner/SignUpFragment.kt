package com.example.panplaner

import android.app.Activity
import android.os.ParcelFileDescriptor
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import java.io.FileNotFoundException
import java.io.IOException
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import android.provider.MediaStore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

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
        selectPhotoSignUp.setOnClickListener {
            Log.d(frag, "photo clicked")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        buttonSubmitSignUp.setOnClickListener {
            Log.d(frag, "Button clicked")
            registerUser()
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun registerUser() {
        val email = editTextEmailSignUp.text.toString()
        val password = editTextPasswordSignUp.text.toString()
        val username = editTextUsernameSignUp.text.toString()
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(activity, "Enter Username, Email and Password", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(frag, "createUserWithEmail: success")
                    val user = auth.currentUser
                    Log.d(frag, user.toString())
                    //uploadImageToStorage()
                    saveUserToDatabase()
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                } else {
                    Log.w(frag, "createUserWithEmail: failure", task.exception)
                }
            }

    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && requestCode == Activity.RESULT_OK && data != null){
            Log.d(frag, "photo selected")
            //hier soll das foto bearbeitet/hochgeladen werden
        }

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

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editTextUsernameSignUp.text.toString())
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(frag, "User saved to db!")
            }
    }
    private fun uploadImageToStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(frag, "uploaded image: ${it.metadata?.path} ")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(frag, "File location: $it")
                }
            }
    }
/*
    fun decodeUri(uri: Uri) {
        var parcelFD: ParcelFileDescriptor? = null
        try {
            parcelFD = contentResolver.openContentResolver(uri, "r")
            val imageSource = parcelFD!!.fileDescriptor

            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(imageSource, null, o)

            // the new size we want to scale to
            val REQUIRED_SIZE = 1024

            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break
                }
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            // decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2)

            selectPhotoSignUp.setImageBitmap(bitmap)

        } catch (e: FileNotFoundException) {
            // handle errors
        } catch (e: IOException) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close()
                } catch (e: IOException) {
                    // ignored
                }

        }
    }
    */

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
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

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}

class User(val uid: String, val username: String)
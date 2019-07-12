package com.example.panplaner

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.xwray.groupie.ViewHolder
import android.util.Log
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_projects.*
import kotlinx.android.synthetic.main.project_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProjectsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "ProjectsFragment"
    private lateinit var projectsRef: DatabaseReference

    private var projectListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.Main){
            loadProjects()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(frag, "onCreateView")
        return inflater.inflate(R.layout.fragment_projects, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val uid = FirebaseAuth.getInstance().uid
        projectsRef = FirebaseDatabase.getInstance().reference.child("/Projects")
        add_project_button.setOnClickListener {
            val action = ProjectsFragmentDirections.actionProjectsFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }

    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    companion object {
        val PROJECT_KEY = "PROJECT_KEY"
    }

    suspend private fun loadProjects() {
        val ref = FirebaseDatabase.getInstance().getReference("/users").child("${FirebaseAuth.getInstance().uid}").child("projects")
        rv_projects.apply {
            layoutManager = LinearLayoutManager(this@ProjectsFragment.context)
            val adapter = GroupAdapter<ViewHolder>()
            adapter.apply {
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d(frag, "$p0")
                        p0.children.forEach {
                            Log.d(frag, "getting data..")
                            Log.d(frag, it.value.toString())
                            val refP = FirebaseDatabase.getInstance().getReference("Projects").child("${it.value}")
                            refP.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    p0.children.forEach {
                                        Log.d(frag, "looking for project")
                                        val project = it.getValue(Project::class.java)
                                        adapter.add(ProjectItem(project))
                                    }
                                    adapter.setOnItemClickListener { item, view ->
                                        val projectItem = item as ProjectItem
                                        val intent = Intent(activity, UserActivity::class.java)
                                        intent.putExtra(PROJECT_KEY, projectItem.project)
                                        startActivity(intent)
                                    }
                                }
                                override fun onCancelled(p0: DatabaseError) {
                                }
                            })
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(activity, "somethin went wrong...", Toast.LENGTH_SHORT).show()
                    }
                })
                rv_projects.adapter = adapter

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}


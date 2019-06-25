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
import com.xwray.groupie.ViewHolder
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_projects.*
import kotlinx.android.synthetic.main.project_list_item.view.*


class ProjectsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "ProjectsFragment"

    private lateinit var projectsRef: DatabaseReference

    private var projectListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val uid = FirebaseAuth.getInstance().uid
        projectsRef = FirebaseDatabase.getInstance().reference.child("/Projects")

        val data = loadProjects()
        add_project_button.setOnClickListener {
            val action = ProjectsFragmentDirections.actionProjectsFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }

    }



    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            Log.d(frag, "row clicked")
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    private fun loadProjects(){
        rv_projects.apply {
            layoutManager = LinearLayoutManager(this@ProjectsFragment.context)
            val adapter = GroupAdapter<ViewHolder>()
            adapter.apply {
                val ref = FirebaseDatabase.getInstance().getReference("/Projects").child("${FirebaseAuth.getInstance().uid}")
                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        p0.children.forEach{
                            val project = it.getValue(Project::class.java)
                            adapter.add(ProjectItem(project))
                            setOnItemClickListener(onItemClick)
                        }
                        rv_projects.adapter = adapter
                    }
                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }
    private val onItemClick = OnItemClickListener { item, view ->
        if(item is ProjectItem){
            Log.d(frag, "clicked ${item.project?.name.toString()}")
            val intent = Intent(context, UserActivity(item.project)::class.java)
            startActivity(intent)
        }
    }
    private fun getProjects() {
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("/Projects").child("${FirebaseAuth.getInstance().uid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val project = it.getValue(Project::class.java)
                    adapter.add(ProjectItem(project))
                    Log.d(frag, it.toString())
                }
                rv_projects.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(frag, "cancelled")
            }
        })
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

class ProjectItem(val project: Project?): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView2.text = project?.name
    }

    override fun getLayout(): Int {
        return R.layout.project_list_item
    }
}
package com.example.panplaner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import android.util.Log
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_projects.*


class ProjectsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val frag = "ProjectsFragment"
    var projects: ArrayList<Project> = ArrayList()

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
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ProjectItem())
        adapter.add(ProjectItem())
        adapter.add(ProjectItem())
        adapter.add(ProjectItem())
        rv_projects.adapter = adapter
        rv_projects.layoutManager = LinearLayoutManager(activity)
        add_project_button.setOnClickListener {
            val action = ProjectsFragmentDirections.actionProjectsFragmentToCreateProjectFragment()
            findNavController().navigate(action)
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

/*    val projectsListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            val project = p0.getValue()
        }
    }
*/
    override fun onAttach(context: Context) {
        super.onAttach(context)

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

class ProjectItem: Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.project_list_item
    }
}
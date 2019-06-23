package com.isd.panplaner

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class DashboardFragment : Fragment() {

    val frag = "DashboardFragment"
    override fun onAttach(context: Context?) {
        Log.d(frag, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(frag, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(frag, "onCreateView")
        return inflater.inflate(R.layout.fragment_dashboard, fragment_holder, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(frag, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(frag, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(frag, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(frag, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(frag, "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(frag, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(frag, "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(frag, "onDetach")
        super.onDetach()
    }
}

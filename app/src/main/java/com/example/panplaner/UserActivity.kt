package com.example.panplaner

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase

class UserActivity : AppCompatActivity() {
    val frag = "UserActivity"
    private lateinit var textMessage: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var projectID: String = ""
    private var projectCreator: String =""

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        Log.d(frag, item.itemId.toString())
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                changeFragment(DashboardFragment(projectID, projectCreator))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dokumente -> {
                changeFragment(DocumentsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val navView = findViewById(R.id.nav_view) as BottomNavigationView
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        checkForUser()
        val project =  intent.getParcelableExtra<Project>(ProjectsFragment.PROJECT_KEY)
        projectID = project.uid
        projectCreator = project.creator
        supportActionBar?.title = project.name
        val fragment = DashboardFragment(project.uid, project.creator)
        val bundle = Bundle()
        bundle.putString("projectID", project.uid)
        bundle.putString("projectName", project.name)
        bundle.putString("projectCreator", project.creator)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.navHostFrag, fragment).setPrimaryNavigationFragment(fragment).commit()
        //changeFragment(fragment)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    private fun putProjectToDashboard(){


        /*
        var projectName = intent.getStringExtra("projectName")
        var projectDeadline = intent.getStringExtra("projectDeadline")
        var projectUsers = intent.getStringExtra("projectUsers")
        var projectUid = intent.getStringExtra("projectUid")
        Log.d(frag, "$projectName")
        Log.d(frag, "$projectDeadline")
        Log.d(frag, "$projectUsers")
        Log.d(frag, "$projectUid")
        var project =  Bundle()
        project.putString("name", "$projectName")
        project.putString("deadline", "$projectDeadline")
        project.putString("users", "$projectUsers")
        project.putString("uid", "$projectUid")
        var dfrag = DashboardFragment()
        dfrag.arguments = project
        changeFragment(dfrag)*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun changeFragment(fragment: Fragment){
        Log.d(frag, "changing...")
        supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFrag, fragment, fragment.tag)
            .addToBackStack(fragment.tag)
            .commit()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_top_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun checkForUser() {
        val user = auth.currentUser
        if (user?.uid == null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        Log.d(frag, user?.uid)
    }
}

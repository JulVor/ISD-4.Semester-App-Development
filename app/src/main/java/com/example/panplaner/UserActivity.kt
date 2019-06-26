package com.example.panplaner

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.content.Intent
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    val frag = "UserActivity"
    private lateinit var textMessage: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                val fragment = DashboardFragment()
                changeFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val fragment = ChatFragment()
                changeFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dokumente -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.tag)
            .addToBackStack(fragment.tag)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        auth = FirebaseAuth.getInstance()
        checkForUser()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        database = FirebaseDatabase.getInstance()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

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
        changeFragment(dfrag)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_top_new_project -> {
                changeFragment(createProjectFragment())
             }
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

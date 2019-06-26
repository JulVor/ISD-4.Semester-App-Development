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
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity(obj: Project?) : AppCompatActivity() {
    constructor(): this(Project())
    val frag = "UserActivity"
    private lateinit var textMessage: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    var project: Project?
    init {
        Log.d(frag, "init")
        project = obj
        Log.d(frag, "${project?.name.toString()}" )
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(DashboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dokumente -> {
                replaceFragment(DocumentsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        auth = FirebaseAuth.getInstance()
        checkForUser()
        Log.d(frag, "${project?.name}")
        replaceFragment(DashboardFragment())
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        database = FirebaseDatabase.getInstance()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return super.onCreateOptionsMenu(menu)
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

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}

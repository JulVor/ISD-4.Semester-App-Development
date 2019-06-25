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
                replaceFragment(DashboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                replaceFragment(ChatFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dokumente -> {
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
            R.id.menu_top_new_project -> {
                replaceFragment(createProjectFragment())
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
    private fun loadProjects() {
        var uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/Projcets/$uid")

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}

package com.example.panplaner

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class UserActivity : AppCompatActivity() {
    val frag = "UserActivity"
    private lateinit var textMessage: TextView
    private lateinit var auth: FirebaseAuth

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(ProjectsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(DashboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                replaceFragment(ChatFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        auth = FirebaseAuth.getInstance()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val user = auth.currentUser
        replaceFragment(ProjectsFragment())
        Log.d(frag, user?.uid)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}

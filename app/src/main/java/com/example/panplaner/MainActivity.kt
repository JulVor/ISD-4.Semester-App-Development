package com.example.panplaner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction().replace(R.id.navFragment, host).setPrimaryNavigationFragment(host).commit()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

    }

}

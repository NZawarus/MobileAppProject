package com.msoe.dndassistant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class SessionViewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // Pass PDF URI to the NavGraph's startDestination
        val pdfUriString = intent.getStringExtra("pdf_uri")
        if (pdfUriString != null && savedInstanceState == null) {
            val bundle = Bundle().apply {
                putString("pdf_uri", pdfUriString)
            }
            navController.setGraph(R.navigation.navgraph, bundle)
        }
    }

}
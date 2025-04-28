package com.msoe.dndassistant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class SessionViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_view)

        // Get the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        // Get the URI from the intent
        val pdfUri = intent.getStringExtra("pdf_uri")

        // If we received a PDF URI, inject it as the start destination argument
        if (pdfUri != null) {
            val navGraph = navController.navInflater.inflate(R.navigation.navgraph)

            // Pass the URI as a bundle to the CharacterSheetFragment
            val bundle = Bundle().apply {
                putString("pdf_uri", pdfUri)
            }

            navGraph.setStartDestination(R.id.CharacterSheetFragment)
            navController.setGraph(navGraph, bundle)
        }

        // Hook up bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }
}

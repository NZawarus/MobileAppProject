package com.msoe.dndassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val dmButton = findViewById<Button>(R.id.btnHost)
        val playerButton = findViewById<Button>(R.id.btnJoin)

        dmButton.setOnClickListener {
            val intent = Intent(this, DMActivity::class.java)
            startActivity(intent)
        }

        playerButton.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }
    }
}
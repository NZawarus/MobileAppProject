package com.msoe.dndassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)

        val joinButton = findViewById<Button>(R.id.btnJoinSession)

        joinButton.setOnClickListener {
            val intent = Intent(this, PlayerSheetActivity::class.java)
            startActivity(intent)
        }
    }
}
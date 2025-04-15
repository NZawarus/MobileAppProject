package com.msoe.dndassistant

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlayerSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player2)


        val loadSheetButton = findViewById<Button>(R.id.btnLoadPdf)

        loadSheetButton.setOnClickListener {
            val toast = Toast.makeText(this, "PDF Button Pressed", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}
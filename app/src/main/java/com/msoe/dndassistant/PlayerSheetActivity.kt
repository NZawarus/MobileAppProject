package com.msoe.dndassistant

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlayerSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player2)
//        com.tom_roush.pdfbox.android.PDFBoxResourceLoader.init(applicationContext)

        val loadSheetButton = findViewById<Button>(R.id.btnLoadPdf)

        loadSheetButton.setOnClickListener {
            // TODO: Figure out PDF loading
            val intent = Intent(this,SessionViewActivity::class.java)
            startActivity(intent)
        }
    }
}
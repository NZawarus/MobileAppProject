package com.msoe.dndassistant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.InputStream

class PlayerSheetActivity : AppCompatActivity() {

    private val PICK_PDF_FILE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player2)

        // Initialize PDFBox
        PDFBoxResourceLoader.init(applicationContext)

        val loadSheetButton = findViewById<Button>(R.id.btnLoadPdf)

        loadSheetButton.setOnClickListener {
            // Launch file picker
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            startActivityForResult(intent, PICK_PDF_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                handlePdf(uri)
            }
        }
    }

    private fun handlePdf(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val document = PDDocument.load(inputStream)
                val title = document.documentInformation?.title ?: "PDF Loaded"
                Toast.makeText(this, "PDF Loaded: $title", Toast.LENGTH_SHORT).show()
                document.close()

                // ✅ Pass URI to SessionViewActivity
                val intent = Intent(this, SessionViewActivity::class.java)
                intent.putExtra("pdf_uri", uri.toString())
                startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading PDF: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

}

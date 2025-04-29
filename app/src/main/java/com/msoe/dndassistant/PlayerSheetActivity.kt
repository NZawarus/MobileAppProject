package com.msoe.dndassistant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.msoe.dndassistant.network.SessionManager

class PlayerSheetActivity : AppCompatActivity() {

    private lateinit var loadSheetButton: Button
    private lateinit var pdfPagesContainer: LinearLayout
    private lateinit var fileNameTextView: TextView

    private val PICK_PDF_FILE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player2)

        val connection = SessionManager.connectionsClient
        val endpointId = SessionManager.endpointId

        loadSheetButton = findViewById(R.id.btnLoadPdf)
        pdfPagesContainer = findViewById(R.id.pdfPagesContainer)
        fileNameTextView = findViewById(R.id.tvPdfFileName)

        loadSheetButton.setOnClickListener {
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
            data?.data?.also { uri ->
                try {
                    val fileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")

                    fileDescriptor?.let {
                        val pdfRenderer = PdfRenderer(it)

                        // Clear previous pages if any
                        pdfPagesContainer.removeAllViews()

                        for (i in 0 until pdfRenderer.pageCount) {
                            val page = pdfRenderer.openPage(i)

                            val bitmap = Bitmap.createBitmap(
                                page.width,
                                page.height,
                                Bitmap.Config.ARGB_8888
                            )

                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                            val imageView = ImageView(this)
                            imageView.setImageBitmap(bitmap)

                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams.setMargins(0, 16, 0, 16)
                            imageView.layoutParams = layoutParams

                            pdfPagesContainer.addView(imageView)

                            page.close()
                        }

                        pdfRenderer.close()

                        fileNameTextView.text = uri.lastPathSegment ?: "PDF Loaded"

                    } ?: run {
                        Toast.makeText(this, "Cannot open file", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to open PDF: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

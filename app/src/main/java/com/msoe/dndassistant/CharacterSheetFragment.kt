package com.msoe.dndassistant

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import java.io.File

class CharacterSheetFragment : Fragment() {

    private lateinit var loadSheetButton: Button
    private lateinit var pdfPagesContainer: LinearLayout
    private lateinit var fileNameTextView: TextView
    private val viewModel: CharacterSheetViewModel by activityViewModels()


    private val PICK_PDF_FILE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PDFBoxResourceLoader.init(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSheetButton = view.findViewById(R.id.btnLoadPdf)
        pdfPagesContainer = view.findViewById(R.id.pdfPagesContainer)
        fileNameTextView = view.findViewById(R.id.tvPdfFileName)

        viewModel.pdfBitmaps.observe(viewLifecycleOwner) { bitmaps ->
            pdfPagesContainer.removeAllViews()
            for (bitmap in bitmaps) {
                val imageView = ImageView(requireContext())
                imageView.setImageBitmap(bitmap)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 16, 0, 16)
                imageView.layoutParams = layoutParams
                pdfPagesContainer.addView(imageView)
            }
        }

        viewModel.pdfFileName.observe(viewLifecycleOwner) { name ->
            fileNameTextView.text = name
        }

        loadSheetButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            startActivityForResult(intent, PICK_PDF_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri -> handlePdf(uri) }
        }
    }


    private fun handlePdf(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val document = PDDocument.load(inputStream)

            // Get the form
            val acroForm = document.documentCatalog.acroForm
            if (acroForm != null) {
                // Force appearance generation
                acroForm.needAppearances = true

                // Refresh all field appearances before flattening
                acroForm.fields.forEach { field ->
                    try {
                        // Make sure the field has a value dictionary
                        if (field.valueAsString != null) {
                            field.setValue(field.valueAsString)
                        }
                    } catch (e: Exception) {
                        // Log specific field issues but continue processing
                        e.printStackTrace()
                    }
                }

                // Commit changes to form before flattening
                document.documentCatalog.acroForm = acroForm

                // Flatten the form - this converts fields to visual elements
                acroForm.flatten()
            }

            val tempFile = File(requireContext().cacheDir, "flattened.pdf")
            document.save(tempFile)
            document.close()

            val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            val bitmaps = mutableListOf<Bitmap>()
            for (i in 0 until pdfRenderer.pageCount) {
                val page = pdfRenderer.openPage(i)

                // Calculate a reasonable scale based on screen width
                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val desiredWidth = screenWidth - 32 // Accounting for some margin
                val scale = desiredWidth.toFloat() / page.width

                // Create a larger bitmap with scaling
                val scaledWidth = (page.width * scale).toInt()
                val scaledHeight = (page.height * scale).toInt()

                val bitmap = Bitmap.createBitmap(
                    scaledWidth,
                    scaledHeight,
                    Bitmap.Config.ARGB_8888 // Using RGB_565 for no transparency and less memory
                )

                bitmap.eraseColor(android.graphics.Color.WHITE)

                // Create a transformation matrix for scaling
                val matrix = android.graphics.Matrix()
                matrix.setScale(scale, scale)

                // Render with the scaling matrix
                page.render(bitmap, null, matrix, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
                bitmaps.add(bitmap)
                page.close()
            }

            pdfRenderer.close()
            fileDescriptor.close()

            viewModel.setPdf(uri, uri.lastPathSegment ?: "PDF Loaded", bitmaps)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to open PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}

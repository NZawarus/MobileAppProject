package com.msoe.dndassistant

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer

class CharacterSheetFragment : Fragment() {

    private lateinit var pdfImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.character_sheet, container, false)
        pdfImageView = view.findViewById(R.id.pdfImageView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uriString = arguments?.getString("pdf_uri")
        if (uriString.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No PDF selected", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = Uri.parse(uriString)

        // Initialize PDFBox
        PDFBoxResourceLoader.init(requireContext().applicationContext)

        try {
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                val document = PDDocument.load(inputStream)
                val renderer = PDFRenderer(document)

                // Render first page at 2x resolution
                val bitmap: Bitmap = renderer.renderImage(0, 2f)
                pdfImageView.setImageBitmap(bitmap)

                document.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error displaying PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

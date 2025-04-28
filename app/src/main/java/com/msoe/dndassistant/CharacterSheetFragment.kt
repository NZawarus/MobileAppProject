package com.msoe.dndassistant

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.IOException

class CharacterSheetFragment : Fragment() {

    companion object {
        private const val TAG = "CharacterSheetFragment"
    }

    private lateinit var pdfImageView: ImageView
    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.character_sheet, container, false)
        pdfImageView = view.findViewById(R.id.pdfImageView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Grab the URI argument
        val uriString = arguments?.getString("pdf_uri")
        Log.d(TAG, "Received pdf_uri argument: $uriString")

        if (uriString.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No PDF selected", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = Uri.parse(uriString)

        // 2) Open renderer and display first page
        try {
            openRenderer(uri)
            showPage(0)
        } catch (e: IOException) {
            Log.e(TAG, "Error opening PDF renderer", e)
            Toast.makeText(
                requireContext(),
                "Error opening PDF: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Throws(IOException::class)
    private fun openRenderer(uri: Uri) {
        // Open the file and create PdfRenderer
        parcelFileDescriptor = requireContext().contentResolver
            .openFileDescriptor(uri, "r")
        parcelFileDescriptor?.let {
            pdfRenderer = PdfRenderer(it)
            Log.d(TAG, "PDF opened, page count = ${pdfRenderer?.pageCount}")
        } ?: throw IOException("Could not open ParcelFileDescriptor")
    }

    private fun showPage(index: Int) {
        pdfRenderer?.let { renderer ->
            if (index < 0 || index >= renderer.pageCount) {
                Log.w(TAG, "Requested page $index out of bounds (0..${renderer.pageCount-1})")
                return
            }

            // Render the page
            val page = renderer.openPage(index)
            val bitmap = Bitmap.createBitmap(
                page.width, page.height,
                Bitmap.Config.ARGB_8888
            )
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfImageView.setImageBitmap(bitmap)
            page.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up
        pdfRenderer?.close()
        parcelFileDescriptor?.close()
    }
}

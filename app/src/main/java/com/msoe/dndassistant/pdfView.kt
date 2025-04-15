package com.msoe.dndassistant

import android.graphics.Bitmap
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDField
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File

class pdfView {
    fun readPdfFormFields(pdfFile: File): Map<String, String> {
        val formData = mutableMapOf<String, String>()
        val document = PDDocument.load(pdfFile)
        val acroForm: PDAcroForm? = document.documentCatalog.acroForm

        acroForm?.fields?.forEach { field ->
            formData[field.fullyQualifiedName] = field.valueAsString
        }

        document.close()
        return formData
    }

    fun renderPdfPage(pdfFile: File, pageIndex: Int): Bitmap? {
        val document = PDDocument.load(pdfFile)
        val renderer = PDFRenderer(document)
        val bitmap = renderer.renderImage(pageIndex, 2f) // 2f = 2x scale for higher res
        document.close()
        return bitmap
    }
}
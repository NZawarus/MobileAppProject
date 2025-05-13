package com.msoe.dndassistant

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CharacterSheetViewModel(application: Application) : AndroidViewModel(application) {

    private val _pdfUri = MutableLiveData<Uri?>()
    val pdfUri: LiveData<Uri?> = _pdfUri

    private val _pdfFileName = MutableLiveData<String>()
    val pdfFileName: LiveData<String> = _pdfFileName

    private val _pdfBitmaps = MutableLiveData<List<Bitmap>>()
    val pdfBitmaps: LiveData<List<Bitmap>> = _pdfBitmaps

    fun setPdf(uri: Uri, fileName: String, bitmaps: List<Bitmap>) {
        _pdfUri.value = uri
        _pdfFileName.value = fileName
        _pdfBitmaps.value = bitmaps
    }

    fun clearPdf() {
        _pdfUri.value = null
        _pdfFileName.value = ""
        _pdfBitmaps.value = emptyList()
    }
}
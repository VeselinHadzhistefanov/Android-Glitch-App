package com.example.Glitchio

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState

class ImageController(val mainActivity: MainActivity){
    lateinit var inputBitmap: MutableState<Bitmap>
    lateinit var outputBitmap: MutableState<Bitmap>
    lateinit var previewBitmaps: MutableList<Bitmap>

    lateinit var bitmapSequence: Array<Bitmap>

    init {
        bitmapSequence = Array(30, {i->Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)})
    }



}
package com.example.Glitchio

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.Glitchio.components_legacy.categories
import com.example.Glitchio.effects.effectCategories
import com.example.Glitchio.effects.getCurrentCategory
import com.example.Glitchio.effects.getCurrentEffect
import com.example.Glitchio.renderer.Renderer
import kotlinx.coroutines.*

val parameters = mutableStateListOf(*Array(6) { 0f })

lateinit var inputBitmap: MutableState<Bitmap>
lateinit var outputBitmap: MutableState<Bitmap>
lateinit var previewBitmaps: MutableList<Bitmap>

class RenderController(val mainActivity: MainActivity) {
    var scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    fun renderEffect() {
        if (!isRendering) {
            scope.launch() {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val effect = getCurrentEffect()
                    val rendererClass = effect.renderer.java
                    val renderer =
                        rendererClass.constructors.first().newInstance(mainActivity) as Renderer
                    val renderedBitmap = renderer.render()

                    withContext(Dispatchers.Main) {
                        outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }

    fun renderPreviews() {
        val currentCategory = getCurrentCategory()
        if (!showControls.value) {
            for (i in 0 until parameters.size) {
                parameters[i] = 0.5f
            }
        }
        for (i in currentCategory.indices) {
            renderPreviewBitmap(categoryIdx.value, i)
        }
    }

    private fun renderPreviewBitmap(categoryIdx: Int, effectIdx: Int) {
        scope.launch {
            withContext(Dispatchers.Default) {
                val rendererClass = effectCategories[categoryIdx][effectIdx].renderer.java
                val renderer =
                    rendererClass.constructors.first().newInstance(mainActivity) as Renderer

                val renderedBitmap = renderer.render()

                withContext(Dispatchers.Main) {
                    previewBitmaps[effectIdx] = renderedBitmap
                }
            }
        }
    }
}
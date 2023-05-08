package com.example.Glitchio

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.Glitchio.components_legacy.categories
import com.example.Glitchio.renderer.Renderer
import kotlinx.coroutines.*

val parameters = mutableStateListOf(*Array(4) { 0f })

class RenderController(val mainActivity: MainActivity) {
    var scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    lateinit var rendererInstance: Renderer
    var rendererCreated = false

    lateinit var inputBitmap: MutableState<Bitmap>
    lateinit var outputBitmap: MutableState<Bitmap>
    lateinit var previewBitmaps: MutableList<Bitmap>

    fun renderEffect() {
        if (!rendererCreated) {
            val rendererClass = categories[0].effects[0].renderer.java
            rendererInstance =
                rendererClass.constructors.first().newInstance(mainActivity) as Renderer
            rendererCreated = true
        }
        if (!isRendering) {
            scope.launch() {
                withContext(Dispatchers.Default) {
                    isRendering = true
                    val parameters = getParameters()
                    val renderer = rendererInstance
                    val renderedBitmap = renderer.render(mainActivity.renderController.inputBitmap.value, parameters)

                    withContext(Dispatchers.Main) {
                        mainActivity.renderController.outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }

    fun renderPreviews() {
        for (i in 0 until currCategory.effects.size) {
            val effect = currCategory.effects[i]
            val params = Array(effect.controls.size) { k -> effect.controls[k].default }
            renderPreviewBitmap(params, categoryIdx.value, i)
        }

    }

    private fun renderPreviewBitmap(params: Array<Float>, categoryIdx: Int, effectIdx: Int) {

        scope.launch {
            withContext(Dispatchers.Default) {

                val rendererClass = categories[categoryIdx].effects[effectIdx].renderer.java
                val renderer =
                    rendererClass.constructors.first().newInstance(mainActivity) as Renderer

                val renderedBitmap =
                    renderer.render(mainActivity.renderController.inputBitmap.value, params)

                withContext(Dispatchers.Main) {
                    mainActivity.renderController.previewBitmaps[effectIdx] = renderedBitmap
                }
            }
        }
    }

    private fun getParameters(): Array<Float> {
        return Array(parameters.size) { i -> parameters[i] }
    }
}
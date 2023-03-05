package com.example.Glitchio.controllers

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.example.Glitchio.*
import com.example.Glitchio.components2.categories
import com.example.Glitchio.renderer.Renderer
import kotlinx.coroutines.*

val parameters = mutableStateListOf(*Array(4) { 0f })

class RenderController(val mainActivity: MainActivity) {

    var scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    lateinit var rendererInstance: Renderer
    var rendererCreated = false

    //var parameters: MutableList<Float> = mutableStateListOf(*Array(4) { 0f })

    lateinit var inputBitmap: MutableState<Bitmap>
    lateinit var outputBitmap: MutableState<Bitmap>
    lateinit var previewBitmaps: MutableList<Bitmap>

    var bitmapSequence: Array<Bitmap> = Array(numFrames) { i ->
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        )
    }


    fun requestRender(params: Array<Float>) {

        if (!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
                    val renderer = rendererClass.constructors.first().newInstance(mainActivity) as Renderer
                    val renderedBitmap = renderer.render(mainActivity.renderController.inputBitmap.value, params)

                    withContext(Dispatchers.Main) {
                        mainActivity.renderController.outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }


    fun renderSequence(){

        scope.launch {
            withContext(Dispatchers.Default) {
                printTime("1")
                val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
                val renderer = rendererClass.constructors.first().newInstance(mainActivity) as Renderer
                printTime("2")

                val bitmapSequence: Array<Bitmap> = Array(numFrames) { i -> Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) }
                printTime("3")
                for (i in 0..149) {
                    //val params = mainActivity.animationController.getParameters(i)
                    val params = arrayOf(0f, 0f, 0f, 0f)
                    val renderedBitmap = renderer.render(mainActivity.renderController.inputBitmap.value, params)
                    bitmapSequence[i] = renderedBitmap
                }
                printTime("4")
            }
        }
    }




    fun requestRenderEffect() {

        val params = mainActivity.animationController.getParameters(0)

        if (!rendererCreated) {
            val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
            rendererInstance =
                rendererClass.constructors.first().newInstance(mainActivity) as Renderer
            rendererCreated = true
        }


        if (!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val renderedBitmap = rendererInstance.render(
                        mainActivity.renderController.inputBitmap.value,
                        params
                    )

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

    fun renderPreviewBitmap(params: Array<Float>, categoryIdx: Int, effectIdx: Int) {

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


}
package com.example.Glitchio

import android.os.Handler
import com.example.Glitchio.components.categories
import com.example.Glitchio.renderer.Renderer
import kotlinx.coroutines.*

class RenderController(val mainActivity: MainActivity) {

    var scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    lateinit var rendererInstance: Renderer
    var rendererCreated = false


    fun requestRender(params: Array<Float>) {

        if (!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java

                    val renderer = rendererClass.constructors.first().newInstance(mainActivity) as Renderer

                    val renderedBitmap = renderer.render(mainActivity.imageController.inputBitmap.value, params)

                    withContext(Dispatchers.Main) {
                        mainActivity.imageController.outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }

    fun runAnimation(){

        val handler: Handler = Handler()
        val delay = 1000L / 30L
        var frame = 0

        handler.postDelayed(object : java.lang.Runnable {
            override fun run() {

                val currImage = mainActivity.imageController.bitmapSequence[frame]
                mainActivity.imageController.outputBitmap.value = currImage
                frame = (frame+1)%30

                handler.postDelayed(this, delay)
            }
        }, delay)

    }


    fun renderSequence(){
        scope.launch {
            withContext(Dispatchers.Default) {

                val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
                val renderer = rendererClass.constructors.first().newInstance(mainActivity) as Renderer


                for (i in 0 .. 29){
                    val currParameters = mainActivity.animationController.getParameters(i)
                    val renderedBitmap = renderer.render(mainActivity.imageController.inputBitmap.value, currParameters)
                    mainActivity.imageController.bitmapSequence[i] = renderedBitmap
                }


                withContext(Dispatchers.Main) {
                    mainActivity.imageController.outputBitmap.value = mainActivity.imageController.bitmapSequence[0]
                    isRendering = false
                }
            }
        }

    }




    fun requestRenderEffect(params: Array<Float>) {

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
                        mainActivity.imageController.inputBitmap.value,
                        params
                    )

                    withContext(Dispatchers.Main) {
                        mainActivity.imageController.outputBitmap.value = renderedBitmap
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
                    renderer.render(mainActivity.imageController.inputBitmap.value, params)

                withContext(Dispatchers.Main) {
                    mainActivity.imageController.previewBitmaps[effectIdx] = renderedBitmap
                }
            }
        }

    }


}
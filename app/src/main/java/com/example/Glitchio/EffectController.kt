package com.example.Glitchio

import com.example.Glitchio.components.categories
import com.example.Glitchio.renderer.Renderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EffectController(val mainActivity: MainActivity) {




    fun requestRender(params: List<Float>) {

        if (!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val rendererClass =
                        categories[categoryIdx.value].effects[effectIdx.value].renderer.java
                    val renderer = rendererClass.constructors.first()
                        .newInstance(mainActivity) as Renderer
                    val renderedBitmap = renderer.render(inputBitmap.value, params)

                    withContext(Dispatchers.Main) {
                        outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }


    fun renderPreviews() {
        for (i in 0 until currCategory.effects.size) {
            val effect = currCategory.effects[i]
            val params = List(effect.controls.size) { k -> effect.controls[k].default }
            renderPreviewBitmap(params, categoryIdx.value, i)
        }

    }

    fun renderPreviewBitmap(params: List<Float>, categoryIdx: Int, effectIdx: Int) {

        scope.launch {
            withContext(Dispatchers.Default) {

                val rendererClass = categories[categoryIdx].effects[effectIdx].renderer.java
                val renderer =
                    rendererClass.constructors.first().newInstance(mainActivity) as Renderer

                val renderedBitmap = renderer.render(inputBitmap.value, params)

                withContext(Dispatchers.Main) {
                    previewBitmaps[effectIdx] = renderedBitmap
                }
            }
        }

    }

    var rendererCreated = false
    lateinit var rendererInstance : Renderer

    fun requestRenderEffect(params : List<Float>) {

        if (!rendererCreated){
            val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
            rendererInstance = rendererClass.constructors.first().newInstance(mainActivity) as Renderer
            rendererCreated = true
        }


        if(!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val renderedBitmap = rendererInstance.render(inputBitmap.value, params)

                    withContext(Dispatchers.Main) {
                        outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }
    }






}
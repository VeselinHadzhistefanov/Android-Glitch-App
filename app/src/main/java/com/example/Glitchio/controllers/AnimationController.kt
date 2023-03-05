package com.example.Glitchio.controllers

import android.os.Handler
import android.os.Looper
import com.example.Glitchio.MainActivity

var numFrames = 150

class AnimationController(val mainActivity: MainActivity) {

    var frame = 0
    private val delay = 1000L / 30L

    var animationRunning = false

    init{
        runAnimation()
    }

    fun startAnimation() {
        animationRunning = true
    }

    fun stopAnimation() {
        animationRunning = false
    }

    fun runAnimation() {
        Handler(Looper.getMainLooper()).postDelayed({
            if(animationRunning) {
                val bitmap = mainActivity.renderController.bitmapSequence[frame]
                mainActivity.renderController.outputBitmap.value = bitmap
                frame = (frame + 1) % numFrames
            }
        }, delay)
    }

    fun getParameters(frame: Int): Array<Float> {
        val paramList: Array<Float> = Array(parameters.size) { i -> parameters[i] }

        for (i in paramList.indices) {
            paramList[i] = sineAnimation(frame.toFloat(), paramList[i], arrayOf())
        }

        return paramList
    }
}
package com.example.Glitchio

import android.os.Handler
import kotlin.math.PI
import kotlin.math.sin

class AnimationController(val mainActivity: MainActivity) {

    var currTime: Double = 0.0

    fun setRenderParameters(
        params: List<Float>,
        movementParams: List<Float> = arrayListOf<Float>()
    ) {
        val handler: Handler = Handler()
        val delay = 1000L / 30L
        handler.postDelayed(object : java.lang.Runnable {
            override fun run() {
                val newParams = arrayOf<Float>()
                val magnitude = 0.1f

                for (parameter in params) {
                    //newParams.add(parameter + sin(currTime).toFloat() * magnitude)
                }
                currTime = (currTime + 0.01) % (Math.PI * 2)


                mainActivity.renderController.requestRender(newParams)
                handler.postDelayed(this, delay)
            }
        }, delay)
    }


    fun getParameters(frame : Int) : Array<Float>{

        val paramList : Array<Float> = Array(mainActivity.parameterController.parameters.size) { i -> mainActivity.parameterController.parameters[i] }

        // Sine function implementation
        for(i in paramList.indices){
            paramList[i] += sin(frame/30f * 2 * PI).toFloat()/20f
        }

        return paramList
    }


}
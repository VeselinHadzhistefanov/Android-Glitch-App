package com.example.Glitchio

import com.example.Glitchio.controllers.parameters

class UIController(val mainActivity: MainActivity) {

    fun onClickEffectCard(idx : Int){
        effectIdx.value = idx
        showControls.value = true

        for (i in currEffect.controls.indices) {
            val control = currEffect.controls[i]
            parameters[i] = control.default
        }

        mainActivity.animationController.startAnimation()
        mainActivity.renderController.requestRender(parameters.toTypedArray())
        mainActivity.renderController.rendererCreated = false
    }


}
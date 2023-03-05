package com.example.Glitchio.core.controls

import com.example.Glitchio.parameters.Parameter
import com.example.Glitchio.parameters.ParameterType

class EffectBuildTemplate {

    init {
        val effect: Effect = Effect("Hue Distortion")
        effect.addPage(0, "Values")
        effect.getPage(0).addControl(SliderControl("Value1", 0))
        effect.getPage(0).addControl(SliderControl("Value2", 1))
        
        effect.addPage(1, "Colors")
        effect.getPage(1).addControl(SliderControl("Color1", 2))
        effect.getPage(1).addControl(SliderControl("Color2", 3))
        effect.getPage(1).addControl(SliderControl("Color3", 4))

    }

}
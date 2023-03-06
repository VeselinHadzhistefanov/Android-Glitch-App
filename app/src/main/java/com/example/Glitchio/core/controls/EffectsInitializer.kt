package com.example.Glitchio.core.controls

import com.example.Glitchio.effectIdx
import com.example.Glitchio.effects

class EffectsInitializer {

    fun initEffects(effects : ArrayList<Effect>) : ArrayList<Effect>{
        val effect = Effect("Hue Distortion")
        effect.addPage(0, "Values")
        effect.getPage(0).add(SliderControl("Value1", 0))
        effect.getPage(0).add(SliderControl("Value2", 1))

        effect.addPage(1, "Colors")
        effect.getPage(1).add(SliderControl("Color1", 2))
        effect.getPage(1).add(SliderControl("Color2", 3))
        effect.getPage(1).add(SliderControl("Color3", 4))
        effects.add(effect)

        return effects
    }

}

fun getCurrentEffect() : Effect{
    //return effects[effectIdx.value]
    return effects[0]
}
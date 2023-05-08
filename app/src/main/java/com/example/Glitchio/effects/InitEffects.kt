package com.example.Glitchio.effects

import android.util.Log
import com.example.Glitchio.effects
import com.example.Glitchio.renderer.Blur
import com.example.Glitchio.renderer.Renderer
import org.reflections.Reflections
import org.reflections.scanners.Scanners

class EffectsInitializer {

    fun initEffects(effects: ArrayList<Effect>): ArrayList<Effect> {

        val reflections = Reflections("com.example.Glitchio")
        val rendererClasses = reflections.getSubTypesOf(Renderer::class.java)

        for (rendererClass in rendererClasses){
            val name = rendererClass.name
            Log.i("Name ======================================================", "sdf")
            Log.i("Name ======================================================", name)
            //effects.add(
        }

        effects.add(Effect("Hue Distortion", Blur::class, "Hue", "Distortion"))

        return effects
    }

}

fun getCurrentEffect(): Effect {
    return effects[0]
    //return effects[effectIdx.value]
}
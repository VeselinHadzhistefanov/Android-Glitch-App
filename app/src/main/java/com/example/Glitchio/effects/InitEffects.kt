package com.example.Glitchio.effects

import com.example.Glitchio.categoryIdx
import com.example.Glitchio.effectIdx
import com.example.Glitchio.renderer.*

val effectCategories: ArrayList<ArrayList<Effect>> = arrayListOf()
val effects: ArrayList<Effect> = arrayListOf()

fun initEffects() {
    val categoryColour: ArrayList<Effect> = arrayListOf()
    categoryColour.add(Effect(ChannelOffset::class, "Channel Offset", "Offset"))
    categoryColour.add(Effect(HueShift::class, "Hue Shift", "Color Shift"))
    categoryColour.add(Effect(HueFocus::class, "Hue Focus", "Color", "Focus Amount"))
    categoryColour.add(Effect(ColorThreshold::class, "Color Threshold", "Color"))
    categoryColour.add(Effect(Rainbow::class, "Rainbow", "Color Multiply", "Color Shift"))
    categoryColour.add(Effect(LightShift::class, "Light Shift", "Shift"))
    categoryColour.add(Effect(LightMultiply::class, "Light Multiply", "Multiply"))

    val categoryDistort: ArrayList<Effect> = arrayListOf()
    categoryDistort.add(Effect(Waterfall::class, "Waterfall", "Height"))
    categoryDistort.add(Effect(Expand::class, "Expand",  "Height"))
    categoryDistort.add(Effect(Wave::class, "Wave", "Displace", "Frequency"))
    categoryDistort.add(Effect(Triangle::class, "Triangle", "Displace", "Frequency"))
    categoryDistort.add(Effect(Hatch::class, "Hatch", "Displace", "Frequency"))

    val categoryDigital: ArrayList<Effect> = arrayListOf()
    categoryDigital.add(Effect(Sort::class, "Sort", "Sort Amount", "Sort Direction"))
    categoryDigital.add(Effect(Blur::class, "Blur", "Blur Amount"))
    categoryDigital.add(Effect(Cloud::class, "Cloud", "Displace", "Size"))
    categoryDigital.add(Effect(Noise::class, "Noise", "Noise Amount", "Size"))

    effectCategories.add(categoryColour)
    effectCategories.add(categoryDistort)
    effectCategories.add(categoryDigital)

    effects.addAll(categoryColour)
    effects.addAll(categoryDistort)
    effects.addAll(categoryDigital)
}

fun getCurrentCategory(): ArrayList<Effect> {
    return effectCategories[categoryIdx.value]
}

fun getCurrentEffect(): Effect {
    return effectCategories[categoryIdx.value][effectIdx.value]
}

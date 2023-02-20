package com.example.Glitchio.components

import com.example.Glitchio.renderer.*
import kotlin.reflect.KClass


class Category(val name: String, val effects : ArrayList<Effect> = arrayListOf()) {}
class Effect(val name: String, val renderer: KClass<*>, val controls: ArrayList<Control> = arrayListOf()) {}
class Control(val name: String, val isHue : Boolean, val isCircular : Boolean, val min : Float, val max : Float, val default : Float){}


val categories = arrayListOf(

    Category("Colour", arrayListOf(
        Effect("Hue Shift", HueShift::class, arrayListOf(
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Hue Focus", HueFocus::class, arrayListOf(
            Control("Colour",true, true, 0f, 1f, 0.5f),
            Control("Amount",false, false, 0f, 100f, 0.5f)
        )),

        Effect("Hue Distort", HueDistort::class, arrayListOf(
            Control("Multiply",false, false, 0f, 10f, 0.5f),
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Value Shift", ValueShift::class, arrayListOf(
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Value Distort", ValueDistort::class, arrayListOf(
            Control("Multiply",false, false, 0f, 10f, 0.5f),
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        )),

    Category("Distort", arrayListOf(
        Effect("Expand", Expand::class, arrayListOf(
            Control("Position",false, false, 0f, 100f, 0.25f),
            Control("Size",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Interpolate", Interpolate::class, arrayListOf(
            Control("Position",false, false, 0f, 100f, 0.25f),
            Control("Size",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Wave", Wave::class, arrayListOf(
            Control("Amplitude",false, false, 0f, 100f, 0.25f),
            Control("Frequency",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Random Distort", RandomDistort::class, arrayListOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Grain",false, false, 0f, 100f, 0.5f)
        ))

    )),


    Category("Digital", arrayListOf(
        Effect("Sort", Sort::class, arrayListOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Direction",false, true, -180f, 180f, 0.5f)
        )),

        Effect("Blur", Blur::class, arrayListOf(
            Control("Amount",false, false, 0f, 100f, 0.5f)
        )),

        Effect("Noise", Noise::class, arrayListOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Grain",false, false, 0f, 100f, 0.5f)
        )),

        ))



)



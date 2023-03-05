package com.example.Glitchio.controllers

import kotlin.math.PI
import kotlin.math.sin

class Animations {

}

fun sineAnimation(frame: Float, value: Float, animationParameters: Array<Float>): Float {

    return value + sin(frame * 2 * PI).toFloat() / 20f
}
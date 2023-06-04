package com.example.Glitchio.effects

import kotlin.reflect.KClass

class Effect(val renderer: KClass<*>, var name: String, vararg controlNames: String) {
    var controls: Array<String> = Array(controlNames.size) { i -> controlNames[i] }
}
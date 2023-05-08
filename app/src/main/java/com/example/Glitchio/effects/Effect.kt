package com.example.Glitchio.effects

import androidx.compose.ui.unit.Dp
import com.example.Glitchio.components.DRAG_AREA_HEIGHT
import kotlin.reflect.KClass

class Effect(var name: String, val renderer: KClass<*>, vararg controlNames: String) {
    var controls: Array<String> = Array(controlNames.size){ i -> controlNames[i]}

    fun getHeight(): Dp {
        return DRAG_AREA_HEIGHT * controls.size
    }
}
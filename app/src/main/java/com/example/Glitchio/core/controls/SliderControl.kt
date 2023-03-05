package com.example.Glitchio.core.controls

import com.example.Glitchio.parameters.Parameter

class SliderControl(
    name: String,
    parameterIdx: Int,
    var defaultValue: Float = 0.5f,
    var minValue: Float = 0f,
    var maxValue: Float = 1f,
) : Control(name, ControlType.SLIDER, parameterIdx) {

    fun getMinText(): String {
        if (maxValue - minValue > 10) {
            return minValue.toInt().toString()
        }
        return minValue.toString().format("%.2f")
    }

    fun getMaxText(): String {
        if (maxValue - minValue > 10) {
            return maxValue.toInt().toString()
        }
        return maxValue.toString().format("%.2f")
    }

    fun getValueText(value: Float): String {
        if (maxValue - minValue > 10) {
            return value.toInt().toString()
        }
        return value.toString().format("%.2f")
    }

}
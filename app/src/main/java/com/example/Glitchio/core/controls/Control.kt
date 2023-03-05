package com.example.Glitchio.core.controls

import com.example.Glitchio.parameters.Parameter
import com.example.Glitchio.parameters.ParameterType

open class Control(
    var name: String,
    var controlType: ControlType,
    var parameterIdx : Int
) {

}
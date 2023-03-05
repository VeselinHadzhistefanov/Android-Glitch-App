package com.example.Glitchio.core.controls

class Page(var name : String = "") {
    var controls : ArrayList<Control> = arrayListOf()

    fun getPageType() : ControlType{
        return controls[0].controlType
    }

    fun addControl(control : Control){
        controls.add(control)
    }
}
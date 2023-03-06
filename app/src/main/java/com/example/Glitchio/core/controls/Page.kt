package com.example.Glitchio.core.controls

class Page(var name: String = "") {
    var controls: ArrayList<Control> = arrayListOf()

    fun add(control: Control) {
        controls.add(control)
    }

    fun getRow(idx: Int): ArrayList<Control> {
        var row: ArrayList<Control> = arrayListOf()
        if (idx == 0) {
            for (c in controls) {
                if (row.size < 3 && c.controlType == ControlType.COLOR)
                    row.add(c)
            }
        } else {
            for (c in controls) {
                if (c.controlType != ControlType.COLOR)
                    row.add(c)
            }
            row = arrayListOf(row[idx])
        }

        return row
    }

    fun getNumColumns() : Int{
        var num = 0
        for (c in controls) {
            if (c.controlType == ControlType.COLOR)
                num = 1

        }
        return controls.size + num
    }

}
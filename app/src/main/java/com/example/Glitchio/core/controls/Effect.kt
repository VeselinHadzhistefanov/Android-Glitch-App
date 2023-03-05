package com.example.Glitchio.core.controls

import com.example.Glitchio.core.controls.*

class Effect(var name : String = "") {
    var pages : ArrayList<Page> = arrayListOf()

    fun addPage(pageIdx: Int, name : String){
        pages[pageIdx] = Page(name)
    }

    fun getPage(pageIdx: Int): Page {
        return pages[pageIdx]
    }

}
package com.example.Glitchio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.Glitchio.core.controls.ControlType
import com.example.Glitchio.core.controls.getCurrentEffect
import com.example.Glitchio.pageIdx

@Composable
fun PageComponent() {
    val effect = getCurrentEffect()
    val page = effect.getPage(pageIdx.value)
    val numColumns = page.controls.size
    val height = DRAG_AREA_HEIGHT * numColumns
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            for (c in page.controls)
                SliderComponent()
        }

    }

}

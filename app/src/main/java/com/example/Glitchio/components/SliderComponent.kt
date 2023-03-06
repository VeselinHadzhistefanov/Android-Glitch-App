package com.example.Glitchio.components

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.ui.theme.*
import kotlin.math.abs

val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics
val DENSITY = displayMetrics.density
val SLIDER_WIDTH = displayMetrics.widthPixels.dp / DENSITY * 0.8f
val SLIDER_HEIGHT = 5.dp
val DRAG_AREA_HEIGHT = 45.dp
val SLIDER_TEXT_SIZE = 12.sp
val SLIDER_TEXT_OFFSET = 18.dp
val DRAG_HANDLE_SIZE = 15.dp


@Composable
fun SliderComponent() {

    val value = remember { mutableStateOf(0f) }

    Box(modifier = Modifier
        .size(SLIDER_WIDTH, DRAG_AREA_HEIGHT)
        .pointerInput(Unit) {
            detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                value.value = (change.position.x.dp / DENSITY / SLIDER_WIDTH).coerceIn(0f, 1f)
            })
        }) {

        SliderBG()
        SliderFill(value.value)
        SliderDragHandle(value.value)
        SliderNameText("VALUE")
        val valueText  = ((value.value - 0.5) * 200).toInt()
        SliderValueText("$valueText%")

    }

}

@Composable
fun SliderBG() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(SLIDER_WIDTH, SLIDER_HEIGHT)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(SliderBG)
        ) {}
    }
}

@Composable
fun SliderFill(value: Float) {
    val width = SLIDER_WIDTH * abs(value - 0.5f)
    var offset = SLIDER_WIDTH / 2
    if (value < 0.5)
        offset = SLIDER_WIDTH * value

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(width, SLIDER_HEIGHT)
                .offset(offset)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(20.dp))
                .background(SliderFill)
        ) {}
    }
}

@Composable
fun SliderDragHandle(value: Float) {
    val offset = SLIDER_WIDTH * value - SLIDER_WIDTH / 2
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(DRAG_HANDLE_SIZE, DRAG_HANDLE_SIZE)
                .offset(offset)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(SliderDragHandle)
        ) {}
    }
}

@Composable
fun SliderNameText(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text, modifier = Modifier
                .size(100.dp, 18.dp)
                .align(Alignment.CenterStart)
                .offset(0.dp, -SLIDER_TEXT_OFFSET),
            color = FontLight,
            fontSize = SLIDER_TEXT_SIZE,
            textAlign = TextAlign.Left
        )
    }
}

@Composable
fun SliderValueText(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text, modifier = Modifier
                .size(100.dp, 18.dp)
                .align(Alignment.CenterEnd)
                .offset(0.dp, -SLIDER_TEXT_OFFSET),
            color = FontLight,
            fontSize = SLIDER_TEXT_SIZE,
            textAlign = TextAlign.Right
        )
    }
}








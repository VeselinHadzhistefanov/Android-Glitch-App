package com.example.Glitchio.components

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
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
import com.example.Glitchio.effects.getCurrentEffect
import com.example.Glitchio.parameters
import com.example.Glitchio.renderController
import com.example.Glitchio.showControls
import com.example.Glitchio.ui.theme.*

val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics
val DENSITY = displayMetrics.density
val SLIDER_HEIGHT = 5.dp
val DRAG_AREA_HEIGHT = 50.dp
val SLIDER_TEXT_SIZE = 12.sp
val SLIDER_TEXT_OFFSET = 20.dp
val DRAG_HANDLE_SIZE = 15.dp


@Composable
fun Slider(name: String, parameterIdx: Int) {
    val value = remember { mutableStateOf(parameters[parameterIdx]) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(DRAG_AREA_HEIGHT)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                    value.value = (change.position.x.dp / DENSITY / (maxWidth - DRAG_HANDLE_SIZE)).coerceIn(0f, 1f)
                    parameters[parameterIdx] = value.value
                    renderController.renderEffect()
                })
            }) {
            if (showControls.value){
                value.value = parameters[parameterIdx]
            }
            SliderBackground()
            SliderFill(value.value)
            SliderDragHandle(value.value)
            SliderName(name)
            val valueText = (value.value * 100).toInt()
            SliderValue("$valueText%")
        }
    }
}

@Composable
fun SliderBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SLIDER_HEIGHT)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(SliderBG)
        ) {}
    }
}

@Composable
fun SliderFill(value: Float) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = maxWidth * value
        Box(
            modifier = Modifier
                .size(width, SLIDER_HEIGHT)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(20.dp))
                .background(SliderFill)
        ) {}
    }
}

@Composable
fun SliderDragHandle(value: Float) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = maxWidth - DRAG_HANDLE_SIZE
        val offset = width * value - width * 0.5f
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
fun SliderName(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            modifier = Modifier
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
fun SliderValue(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            modifier = Modifier
                .size(100.dp, 18.dp)
                .align(Alignment.CenterEnd)
                .offset(0.dp, -SLIDER_TEXT_OFFSET),
            color = FontLight,
            fontSize = SLIDER_TEXT_SIZE,
            textAlign = TextAlign.Right
        )
    }
}








package com.example.Glitchio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.Glitchio.R
import com.example.Glitchio.effects.getCurrentEffect
import com.example.Glitchio.showControls
import com.example.Glitchio.ui.theme.*

val ACCEPT_BUTTON_SIZE = 22.dp
val TOP_MARGIN = 15.dp
val BOTTOM_MARGIN = 15.dp

@Composable
fun ControlsUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        val height = getHeight()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.BottomCenter)
                .background(DarkBG)
        ) {

            Column() {
                Spacer(modifier = Modifier.height(TOP_MARGIN))
                SlidersUI()
            }

            AcceptButton()
            BackButton()
        }
    }
}

@Composable
fun SlidersUI() {
    val effect = getCurrentEffect()
    val height = effect.getHeight()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            for (i in effect.controls.indices) {
                Slider(effect.controls[i], i)
            }
        }

    }
}

@Composable
fun AcceptButton() {

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(modifier = Modifier
            .align(Alignment.TopEnd),
            onClick = {
                showControls.value = false
            }
        ) {
            Icon(
                modifier = Modifier.size(ACCEPT_BUTTON_SIZE),
                painter = painterResource(id = R.drawable.tick),
                contentDescription = null,
                tint = IconLargeLight,
            )
        }
    }
}

@Composable
fun BackButton() {

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(modifier = Modifier
            .align(Alignment.TopStart),
            onClick = {
                showControls.value = false
            }
        ) {
            Icon(
                modifier = Modifier.size(ACCEPT_BUTTON_SIZE),
                painter = painterResource(id = R.drawable.undo),
                contentDescription = null,
                tint = IconLargeLight
            )
        }
    }
}

fun getHeight(): Dp {
    return getCurrentEffect().getHeight() + BOTTOM_MARGIN + TOP_MARGIN
}

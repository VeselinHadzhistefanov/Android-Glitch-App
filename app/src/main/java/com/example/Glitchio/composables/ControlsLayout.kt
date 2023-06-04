package com.example.Glitchio.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.R
import com.example.Glitchio.effects.getCurrentEffect
import com.example.Glitchio.inputBitmap
import com.example.Glitchio.outputBitmap
import com.example.Glitchio.showControls
import com.example.Glitchio.ui.theme.*

val ACCEPT_BUTTON_SIZE = 24.dp
val TOP_BAR_HEIGHT = 24.dp
val MID_MARGIN = 20.dp
val SPACER_SIZE = 10.dp

@Composable
fun ControlsLayout() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(visible = showControls.value,
                enter = slideInVertically { height -> height * 2 } + fadeIn(),
                exit = slideOutVertically { height -> height * 2 } + fadeOut()) {
                Box(modifier = Modifier.background(DarkGray)) {
                    Column(
                        modifier = Modifier
                            .padding(25.dp, 25.dp, 25.dp, 15.dp)
                    ) {
                        val effect = getCurrentEffect()
                        TopBar(effect.name)
                        Spacer(modifier = Modifier.height(MID_MARGIN))

                        for (i in getCurrentEffect().controls.indices) {
                            Spacer(modifier = Modifier.height(SPACER_SIZE))
                            Slider(effect.controls[i], i)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(name: String) {
    Box(
        modifier = Modifier
            .height(TOP_BAR_HEIGHT)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = FontLight,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
        AcceptButton()
        BackButton()
    }
}

@Composable
fun AcceptButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .size(ACCEPT_BUTTON_SIZE)
                .align(Alignment.BottomEnd)
                .clickable {
                    showControls.value = false
                    inputBitmap.value = outputBitmap.value
                },
            painter = painterResource(id = R.drawable.tick),
            contentDescription = null,
            tint = IconLargeLight
        )
    }
}

@Composable
fun BackButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .size(ACCEPT_BUTTON_SIZE)
                .align(Alignment.BottomStart)
                .clickable {
                    showControls.value = false
                    outputBitmap.value = inputBitmap.value
                },
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            tint = IconLargeLight
        )
    }
}
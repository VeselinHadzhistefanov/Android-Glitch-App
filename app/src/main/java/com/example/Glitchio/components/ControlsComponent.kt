package com.example.Glitchio.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Glitchio.core.controls.getCurrentEffect
import com.example.Glitchio.pageIdx
import com.example.Glitchio.showControls
import com.example.Glitchio.ui.theme.DarkBG


@Composable
fun ControlsComponent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(DarkBG)) {
            Spacer(modifier = Modifier.height(50.dp))
            PageComponent()
        }
    }
}
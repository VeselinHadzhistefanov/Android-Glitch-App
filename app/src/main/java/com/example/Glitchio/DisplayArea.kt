package com.example.Glitchio

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

@Composable
fun DisplayArea() {
    Box() {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        if (showControls.value){
                            parameters[2] = (parameters[2] + pan.x / displayMetrics.widthPixels).coerceIn(0f, 1f)
                            parameters[3] = (parameters[3] - pan.y / displayMetrics.widthPixels).coerceIn(0f, 1f)
                            parameters[4] = (parameters[4] + zoom / displayMetrics.widthPixels).coerceIn(0f, 1f)
                            parameters[5] = (parameters[5] + rotation).coerceIn(0f, 1f)

                            Log.i("Pan: ", "x = " + parameters[2].toString() + " y = " + parameters[3].toString())
                            Log.i("Zoom, rotation: ", "zoom = " + parameters[4].toString() + " rotation = " + parameters[5].toString())
                            renderController.renderEffect()
                        }
                    }
                },
            bitmap = outputBitmap.value.asImageBitmap(),
            contentDescription = null
        )
    }
}

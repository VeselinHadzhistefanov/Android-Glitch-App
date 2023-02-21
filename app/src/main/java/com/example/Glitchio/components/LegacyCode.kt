package com.example.Glitchio.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.*
import com.example.Glitchio.ui.theme.LightFont
import com.example.Glitchio.ui.theme.MidDarkGray
import com.example.Glitchio.ui.theme.MidGray

class LegacyCode {
}

/*
@Composable
fun CustomSlider(context : Context, parameterIdx : Int, isHue : Boolean = true, isCircular : Boolean, min : Float = 0f, max : Float = 1f){
    val width = 300.dp
    val height = 40.dp
    val precision = 2f

    val offset = remember { mutableStateOf(0f) }

    val scrollableState = rememberScrollableState(consumeScrollDelta = {delta ->
        parameters[parameterIdx] = parameters[parameterIdx] - pxToDp(delta.toInt()) / width.value / precision

        if(isCircular){
            parameters[parameterIdx] = (parameters[parameterIdx]+1)%1
        }
        else{
            parameters[parameterIdx] = java.lang.Float.min(parameters[parameterIdx], 1f)
            parameters[parameterIdx] = java.lang.Float.max(parameters[parameterIdx], 0f)
        }

        val activity = context as MainActivity
        activity.renderController.requestRender(parameters)

        delta
    })

    offset.value = -(parameters[parameterIdx] * width.value * precision.toInt())



    Box(modifier = Modifier
        .size(width, height)
        .border(1.dp, MidDarkGray)) {

        if(!isHue) {
            TextWithShadow(
                text = formatValue(parameters[parameterIdx], min, max),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, (-22).dp),
                color = LightFont
            )
        }
        Box(
            modifier = Modifier
                .size(width * precision + width, height)
                .scrollable(scrollableState, Orientation.Horizontal)
                .clipToBounds()
                .horizontalScroll(rememberScrollState(), false)
                .offset(offset.value.dp + width / 2, 0.dp)

        ) {

            Surface(
                modifier = Modifier
                    .size(width * precision, height)
                    .alpha(0.2f), color = MidGray
            ) {}

            Divider(
                color = LightFont, thickness = 2.dp, modifier = Modifier
                    .width(width * precision)
                    .height(1.dp)
                    .offset(0.dp, height / 2)
            )

            val indicators = 32

            for (i in 0..indicators) {
                Divider(
                    color = LightFont, thickness = 1.dp, modifier = Modifier
                        .width(1.dp)
                        .height(
                            if (i == 0 || i == indicators) height
                            else if (i % (indicators / 4) == 0) height * 0.6f
                            else height * 0.3f
                        )
                        .offset(width * precision * i / indicators, 0.dp)
                        .align(Alignment.CenterStart)
                )
            }

            if(isHue){
                HueGradient(modifier = Modifier
                    .size(width * precision, height)
                    .offset(-width * precision))
                HueGradient(modifier = Modifier.size(width * precision, height))
                HueGradient(modifier = Modifier
                    .size(width * precision, height)
                    .offset(width * precision))
            }


        }
        Divider(
            color = LightFont, thickness = 1.dp, modifier = Modifier
                .width(1.dp)
                .height(height)
                .offset(width / 2, 0.dp)
                .align(Alignment.CenterStart)
        )




    }

}

*/
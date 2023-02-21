package com.example.Glitchio

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.ui.theme.*

class EffectControls (val mainActivity: MainActivity){


    @Composable
    fun ControlsLayout(context: Context) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //BackButton()
            ParameterControls(context)
        }
    }

    @Composable
    fun ParameterControls(context: Context) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            VerticalGradient(
                modifier = Modifier.fillMaxSize(),
                DarkGray.value(0.05f),
                DarkGray.value(-0.05f)
            )

            TextWithShadow(
                text = currEffect.name,
                color = MidLightGray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, -35.dp)
            )

            Box(
                Modifier
                    .offset(0.dp, 0.dp)
                    .align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .offset(10.dp, 10.dp)
                        .align(Alignment.Center)
                ) {
                    Spacer(Modifier.height(5.dp))

                    for (i in 0 until currEffect.controls.size) {
                        val control = currEffect.controls[i]
                        SliderLayout(
                            context, i,
                            control.isHue,
                            control.isCircular,
                            control.min,
                            control.max
                        )
                    }
                }
            }
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(0.dp, -45.dp)
            ) {
                AcceptButton()
            }
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .offset(0.dp, -45.dp)
            ) {
                BackButton()
            }

        }


    }


// Constants =========================================================================

    class ValuePair(val x: Dp, val y: Dp) {}

    val sliderSize = ValuePair(250.dp, 6.dp)
    val sliderDragArea = ValuePair(sliderSize.x, 45.dp)


    // Main Slider UI ===========================================================================
    @Composable
    fun SliderLayout(
        context: Context,
        parameterIdx: Int,
        isHue: Boolean = true,
        isCircular: Boolean,
        min: Float = 0f,
        max: Float = 1f
    ) {


        if (!isHue) {
            DragSlider(context, parameterIdx)
        }
        if (isHue) {
            HueSlider(parameterIdx)
        }


    }

// Slider types ===========================================================================

    @Composable
    fun DragSlider(context: Context, parameterIdx: Int) {

        Box(modifier = Modifier
            .size(sliderDragArea.x, sliderDragArea.y)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->

                    mainActivity.parameterController.parameters[parameterIdx] =
                        (change.position.x / sliderDragArea.x.toPx()).coerceIn(0f, 1f)
                    val activity = context as MainActivity

                    //activity.requestRender(parameters)
                    //activity.renderController.requestRenderEffect(mainActivity.parameterController.parameters.toTypedArray())

                    mainActivity.renderController.renderSequence()
                    mainActivity.renderController.runAnimation()

                })
            })
        {
            Box(modifier = Modifier.offset(0.dp, 10.dp)) {

                val value = sliderSize.x * mainActivity.parameterController.parameters[parameterIdx]
                val displayValue = (mainActivity.parameterController.parameters[parameterIdx] * 1000).toInt() / 10f

                SliderText(currEffect.controls[parameterIdx].name)
                SliderValue(displayValue.toString())
                SliderBG()

                Box(
                    modifier = Modifier
                        .size(value, sliderSize.y)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    VerticalGradient(
                        modifier = Modifier.fillMaxSize(),
                        color1 = MidLightGray.value(0.2f),
                        color2 = MidLightGray.value(-0.4f)
                    )
                }

                SliderDragHandle(value)
            }
        }

    }


    @Composable
    fun DualSlider(parameterIdx1: Int, parameterIdx2: Int, ) {

        val value1 = sliderSize.x * mainActivity.parameterController.parameters[parameterIdx1]
        val value2 = sliderSize.x * mainActivity.parameterController.parameters[parameterIdx2]
        val displayValue = (mainActivity.parameterController.parameters[parameterIdx1] * 1000).toInt() / 10f

        SliderText(currEffect.controls[parameterIdx1].name)
        SliderValue(displayValue.toString())
        SliderBG()

        Box(
            modifier = Modifier
                .size(value1, sliderSize.y)
                .clip(RoundedCornerShape(20.dp))
        ) {
            VerticalGradient(
                modifier = Modifier.fillMaxSize(),
                color1 = MidLightGray.value(0.2f),
                color2 = MidLightGray.value(-0.4f)
            )
        }

        SliderDragHandle(value1)
        SliderDragHandle(value2)

    }


    @Composable
    fun SliderBG() {
        Box(
            modifier = Modifier
                .size(sliderSize.x, sliderSize.y)
                .clip(RoundedCornerShape(20.dp))
        ) {
            VerticalGradient(
                modifier = Modifier.fillMaxSize(),
                color1 = MidDarkGray.value(0.1f),
                color2 = MidDarkGray.value(-0.1f)
            )
        }
    }

    @Composable
    fun SliderText(text: String) {
        TextWithShadow(
            text = text, modifier = Modifier
                .size(100.dp, 20.dp)
                .offset(-110.dp, -7.dp),
            color = MidGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )
    }

    @Composable
    fun SliderValue(text: String) {
        TextWithShadow(
            text = text, modifier = Modifier
                .size(100.dp, 20.dp)
                .offset(sliderSize.x + 10.dp, -7.dp),
            color = MidGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Left
        )
    }

    @Composable
    fun SliderDragHandle(value: Dp) {
        Box(
            modifier = Modifier
                .size(15.dp, 15.dp)
                .offset(value - 7.5.dp, -4.5.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            VerticalGradient(
                modifier = Modifier.fillMaxSize(),
                color1 = LightGray.value(0.2f),
                color2 = LightGray.value(-0.2f)
            )
        }
    }


    @OptIn(ExperimentalGraphicsApi::class)
    @Composable
    fun HueSlider(parameterIdx: Int) {

        val value = sliderSize.x * mainActivity.parameterController.parameters[parameterIdx]
        val displayValue = (mainActivity.parameterController.parameters[parameterIdx] * 1000).toInt() / 10f


        TextWithShadow(
            text = currEffect.controls[parameterIdx].name, modifier = Modifier
                .size(100.dp, 20.dp)
                .offset(-110.dp, -7.dp),
            color = MidGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Right
        )

        TextWithShadow(
            text = displayValue.toString(), modifier = Modifier
                .size(100.dp, 20.dp)
                .offset(sliderSize.x + 10.dp, -7.dp),
            color = MidGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Left
        )


        HueGradient(
            modifier = Modifier
                .size(sliderSize.x, sliderSize.y)
                .clip(RoundedCornerShape(20.dp))
                .alpha(0.5f)
        )

        Box(
            modifier = Modifier
                .size(15.dp, 15.dp)
                .offset(value - 7.5.dp, -4.5.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.hsv(360f * mainActivity.parameterController.parameters[parameterIdx], 1f, 1f))
        ) {}


    }


    @Composable
    fun AcceptButton() {

        IconButton(
            onClick = {
                showControls.value = false
                controlIdx.value = 0
                mainActivity.imageController.inputBitmap.value = mainActivity.imageController.outputBitmap.value
            }
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Localized description",
                tint = MidGray,
                modifier = Modifier.size(28.dp)
            )
        }

    }

    @Composable
    fun BackButton() {

        IconButton(
            onClick = {
                showControls.value = false
                controlIdx.value = 0
                mainActivity.imageController.outputBitmap.value = mainActivity.imageController.inputBitmap.value
            }
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Localized description",
                tint = MidGray,
                modifier = Modifier.size(28.dp)
            )
        }


    }


// Utilities ===========================================================================

    @OptIn(ExperimentalGraphicsApi::class)
    @Composable
    fun HueGradient(modifier: Modifier) {

        Canvas(modifier = modifier) {

            val colorList: List<Color> = listOf(
                Color.hsv(0f, 1f, 1f),
                Color.hsv(120f, 1f, 1f),
                Color.hsv(240f, 1f, 1f),
                Color.hsv(0f, 1f, 1f),
            )
            val brush = Brush.horizontalGradient(
                colors = colorList,
                tileMode = TileMode.Repeated
            )
            drawRect(
                brush = brush,
                size = size
            )
        }

    }

    @OptIn(ExperimentalGraphicsApi::class)
    @Composable
    fun VerticalGradient(modifier: Modifier, color1: Color, color2: Color) {

        Canvas(modifier = modifier) {

            val colorList: List<Color> = listOf(color1, color2)

            val brush = Brush.verticalGradient(
                colors = colorList,
                tileMode = TileMode.Repeated
            )
            drawRect(
                brush = brush,
                size = size
            )
        }

    }
}



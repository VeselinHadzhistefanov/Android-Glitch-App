package com.example.Glitchio

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.hsv
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.renderer.*
import com.example.Glitchio.ui.theme.*
import java.lang.Math.*
import kotlin.reflect.KClass

class Category(val name: String, val effects : List<Effect>) {}
class Effect(val name: String, val renderer: KClass<*>, val controls: List<Control>) {}
class Control(val name: String, val isHue : Boolean, val isCircular : Boolean, val min : Float, val max : Float, val default : Float){}

val categories = listOf(

    Category("Colour", listOf(
        Effect("Hue Shift", HueShift::class, listOf(
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Hue Focus", HueFocus::class, listOf(
            Control("Colour",true, true, 0f, 1f, 0.5f),
            Control("Amount",false, false, 0f, 100f, 0.5f)
        )),

        Effect("Hue Distort", HueDistort::class, listOf(
            Control("Multiply",false, false, 0f, 10f, 0.5f),
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Value Shift", ValueShift::class, listOf(
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

        Effect("Value Distort", ValueDistort::class, listOf(
            Control("Multiply",false, false, 0f, 10f, 0.5f),
            Control("Shift",false, true, 0f, 100f, 0.5f)
        )),

    )),

    Category("Distort", listOf(
        Effect("Expand", Expand::class, listOf(
            Control("Position",false, false, 0f, 100f, 0.25f),
            Control("Size",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Interpolate", Interpolate::class, listOf(
            Control("Position",false, false, 0f, 100f, 0.25f),
            Control("Size",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Wave", Wave::class, listOf(
            Control("Amplitude",false, false, 0f, 100f, 0.25f),
            Control("Frequency",false, false, 0f, 100f, 0.25f)
        )),

        Effect("Random Distort", RandomDistort::class, listOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Grain",false, false, 0f, 100f, 0.5f)
        ))

    )),


    Category("Digital", listOf(
        Effect("Sort", Sort::class, listOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Direction",false, true, -180f, 180f, 0.5f)
        )),

        Effect("Blur", Blur::class, listOf(
            Control("Amount",false, false, 0f, 100f, 0.5f)
        )),

        Effect("Noise", Noise::class, listOf(
            Control("Amount",false, false, 0f, 100f, 0.5f),
            Control("Grain",false, false, 0f, 100f, 0.5f)
        )),

    ))



)




@Composable
fun ControlsLayout(context : Context){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f), color = PurpleGrey40
        ) {}
        TopBar()
        AcceptButton()

    }

    Column() {
        Spacer(modifier = Modifier.height(40.dp))
        ParameterControls(context)
    }

}


@Composable
fun TopBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Surface(
            modifier = Modifier
                .alpha(0.2f)
                .fillMaxSize(), color = PurpleGrey40
        ) {}


        Row(){


            TextWithShadow(
                text = currEffect.name,
                color = MidFont,
                modifier = Modifier.padding(16.dp, 8.dp),
                fontSize = 17.sp
            )



            val controls = currEffect.controls
            for (i in controls.indices) {

                Box(modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
                    .clickable { controlIdx.value = i },
                    contentAlignment = Alignment.Center) {

                    if (controlIdx.value == i) {
                        Divider(
                            color = LightFont, modifier = Modifier
                                .height(2.dp)
                                .align(Alignment.BottomCenter)
                        )
                        Surface(modifier = Modifier.fillMaxSize().alpha(0.05f), color = LightFont, ){}
                    }

                    TextWithShadow(
                        text = controls[i].name,
                        color = if (controlIdx.value == i) LightFont else MidFont,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(10.dp, 10.dp)
                    )
                }
            }
        }

        IconButton(
            onClick = {
                showControls.value = false
                controlIdx.value = 0
                outputBitmap.value = inputBitmap.value
                      },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Localized description",
                tint = MidFont,
                modifier = Modifier.size(28.dp)
            )
        }

    }

}


@Composable
fun AcceptButton(){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        IconButton(
            onClick = { showControls.value = false
                controlIdx.value = 0
                inputBitmap.value = outputBitmap.value},
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Localized description",
                tint = MidFont,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}



@Composable
fun ParameterControls(context : Context){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp, 0.dp)
    ) {
        Column() {
            Spacer(modifier = Modifier.height(40.dp))

            val currentControl = currControl
            CustomSlider(context, controlIdx.value,
                currentControl.isHue,
                currentControl.isCircular,
                currentControl.min,
                currentControl.max)
        }



    }




}



@Composable
fun CustomSlider(context : Context, parameterIdx : Int, isHue : Boolean = true, isCircular : Boolean, min : Float = 0f, max : Float = 1f){
    val width = 300.dp
    val height = 40.dp
    val precision = 2f

    val offset = remember { mutableStateOf(0f) }

    val scrollableState = rememberScrollableState(consumeScrollDelta = {delta ->
        parameters[parameterIdx] = parameters[parameterIdx] - pxToDp(delta.toInt())/ width.value / precision

        if(isCircular){
            parameters[parameterIdx] = (parameters[parameterIdx]+1)%1
        }
        else{
            parameters[parameterIdx] = min(parameters[parameterIdx], 1f)
            parameters[parameterIdx] = max(parameters[parameterIdx], 0f)
        }

        val activity = context as MainActivity
        activity.requestRender(parameters)

        delta
    })

    offset.value = -(parameters[parameterIdx] * width.value * precision.toInt())



    Box(modifier = Modifier
        .size(width, height)
        .border(1.dp, PurpleGrey40)) {

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
                    .alpha(0.2f), color = Purple40
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

fun formatValue(value : Float, min: Float, max : Float) : String{
    val valMapped = value * (max-min) + min
    if (max - min > 1f) return valMapped.toInt().toString()
    else return valMapped.toString()
}




@Composable
fun TextWithShadow(text: String, modifier: Modifier = Modifier, color: Color,
                   fontSize: TextUnit = 12.sp,
                   textAlign : TextAlign = TextAlign.Center,
                   textDecoration : TextDecoration = TextDecoration.None) {
    Box() {
        Text(
            text = text,
            fontSize = fontSize,
            textAlign = textAlign,
            textDecoration = textDecoration,
            color = Color.Black,
            modifier = modifier
                .offset(1.dp, 1.dp)
                .alpha(0.5f)
        )
        Text(
            text = text,
            fontSize = fontSize,
            textAlign = textAlign,
            textDecoration = textDecoration,
            color = color,
            modifier = modifier
        )
    }
}



@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun HueGradient(modifier : Modifier){

    Canvas(modifier = modifier) {

        val colorList: List<Color> = listOf(
            hsv(0f, 1f, 1f),
            hsv(120f, 1f, 1f),
            hsv(240f, 1f, 1f),
            hsv(0f, 1f, 1f),
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


fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().getDisplayMetrics().density).toInt()
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
}
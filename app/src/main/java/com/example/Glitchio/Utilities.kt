package com.example.Glitchio

import android.content.res.Resources
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColor

fun formatValue(value : Float, min: Float, max : Float) : String{
    val valMapped = value * (max-min) + min
    if (max - min > 1f) return valMapped.toInt().toString()
    else return valMapped.toString()
}


fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().getDisplayMetrics().density).toInt()
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
}



@OptIn(ExperimentalGraphicsApi::class)
fun modifyColor(color : Color, h : Float, s : Float, v: Float) : Color{


    val hsv = FloatArray(3)
    ColorUtils.RGBToHSL((color.red*255).toInt(), (color.green*255).toInt(), (color.blue*255).toInt(), hsv)
    hsv[0] = (hsv[0] + h)%360f
    hsv[1] = (hsv[1] + s).coerceIn(0f, 1f)
    hsv[2] = (hsv[2] + v).coerceIn(0f, 1f)

    return Color.hsl(hsv[0], hsv[1], hsv[2], 1.0f)
}



fun Color.hue(hue : Float) : Color{
    return modifyColor(this, hue, 0f, 0f)
}
fun Color.saturation(saturation : Float) : Color{
    return modifyColor(this, 0f, saturation, 0f)
}
fun Color.value(value : Float) : Color{
    return modifyColor(this, 0f, 0f, value)
}
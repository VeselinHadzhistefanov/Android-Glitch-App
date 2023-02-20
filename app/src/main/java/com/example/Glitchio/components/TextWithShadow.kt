package com.example.Glitchio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextWithShadow(text: String, modifier: Modifier = Modifier, color: Color,
                   fontSize: TextUnit = 12.sp,
                   textAlign : TextAlign = TextAlign.Center,
                   textDecoration : TextDecoration = TextDecoration.None,
                   fontWeight : FontWeight = FontWeight.Light) {
    Box() {
        Text(
            text = text,
            fontSize = fontSize,
            textAlign = textAlign,
            textDecoration = textDecoration,
            color = Color.Black,
            fontWeight = fontWeight,
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
            fontWeight = fontWeight,
            modifier = modifier
        )
    }
}
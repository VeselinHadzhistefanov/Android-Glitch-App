package com.example.Glitchio


import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.components_legacy.categories
import com.example.Glitchio.effects.effectCategories
import com.example.Glitchio.ui.theme.*


val EFFECT_CARD_IMAGE_SIZE = 90.dp
val EFFECT_CARD_TEXT_BOX_SIZE = 20.dp
val CATEGORIES_ROW_HEIGHT = 50.dp
val TOP_MARGIN = 25.dp
val CARD_SPACING = 5.dp
val CATEGORIES_ICON_SIZE = 25.dp

@Composable
fun EffectSelectionLayout() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            AnimatedVisibility(visible = !showControls.value,
                enter = slideInVertically { height -> height * 2 } + fadeIn(),
                exit = slideOutVertically { height -> height * 2 } + fadeOut()) {
                Box(modifier = Modifier.background(DarkGray)) {
                    Column() {
                        Spacer(modifier = Modifier.height(TOP_MARGIN))
                        EffectsRow()
                        Spacer(modifier = Modifier.height(5.dp))
                        CategoriesRow()
                    }
                }
            }
        }
    }
}

@Composable
fun EffectCard(categoryIdx: Int, idx: Int) {
    Box(modifier = Modifier
        .width(EFFECT_CARD_IMAGE_SIZE + 20.dp)
        .wrapContentHeight()
        .clickable {
            onClickEffectCard(idx)
        }) {
        Column {
            Image(
                modifier = Modifier
                    .size(EFFECT_CARD_IMAGE_SIZE)
                    .align(Alignment.CenterHorizontally),
                bitmap = previewBitmaps[idx].asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(EFFECT_CARD_TEXT_BOX_SIZE),
                text = effectCategories[categoryIdx][idx].name,
                fontSize = 12.sp,
                color = LightFont,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }
}

@Composable
fun EffectsRow() {
    renderController.renderPreviews()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clipToBounds()
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            for (k in 0 until effectCategories[categoryIdx.value].size) {
                Spacer(modifier = Modifier.width(CARD_SPACING))
                EffectCard(categoryIdx.value, k)
            }
        }
    }
}

@Composable
fun CategoriesRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row() {
            val icons = arrayListOf(R.drawable.icon_color, R.drawable.icon_distort, R.drawable.icon_digital)
            for (i in 0 until categories.size) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(CATEGORIES_ROW_HEIGHT)
                        .clickable { categoryIdx.value = i })
                {
                    Icon(
                        modifier = Modifier
                            .size(CATEGORIES_ICON_SIZE)
                            .align(Alignment.Center)
                            .clickable {
                                categoryIdx.value = i
                            },
                        painter = painterResource(id = icons[i]),
                        contentDescription = null,
                        tint = if (categoryIdx.value == i) LightFont else MidFont
                    )
                }
            }
        }
    }
}

fun onClickEffectCard(idx: Int) {
    effectIdx.value = idx
    showControls.value = true

    for (i in parameters.indices) {
        parameters[i] = 0.5f
    }

    renderController.renderEffect()
}

fun onClickRandomEffect() {
    categoryIdx.value = (0..2).random()
    effectIdx.value = (0 until effectCategories[categoryIdx.value].size).random()
    showControls.value = true

    for (i in parameters.indices) {
        parameters[i] = Math.random().toFloat()
    }

    renderController.renderEffect()
}
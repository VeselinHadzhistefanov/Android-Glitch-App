package com.example.Glitchio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.components.ControlsUI
import com.example.Glitchio.components_legacy.*
import com.example.Glitchio.effects.EffectsInitializer
import com.example.Glitchio.ui.theme.*
import java.io.File
import java.io.FileOutputStream

// Coroutines =====================================
//val scope = CoroutineScope(Job() + Dispatchers.Main)

// UI =====================================
lateinit var categoryIdx: MutableState<Int>
lateinit var effectIdx: MutableState<Int>
lateinit var controlIdx: MutableState<Int>
lateinit var showControls: MutableState<Boolean>
lateinit var showDefaultScreen: MutableState<Boolean>
var isRendering = false

val currCategory: Category get() = categories[categoryIdx.value]
val currEffect: Effect get() = currCategory.effects[effectIdx.value]
val currControl: Control get() = if (controlIdx.value < currEffect.controls.size) currEffect.controls[controlIdx.value] else currEffect.controls[0]

var inputImageUri: Uri? = null


val effectCardHeight = 125.dp


var effects: ArrayList<com.example.Glitchio.effects.Effect> = arrayListOf()
lateinit var pageIdx: MutableState<Int>

class MainActivity : ComponentActivity() {

    var renderController: RenderController = RenderController(this)

    var effectControls: EffectControls = EffectControls(this)

    companion object {
        private lateinit var instance: MainActivity

        fun getInstance(): MainActivity {
            return instance
        }
    }


    // OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        val effectInitializer = EffectsInitializer()
        effects = effectInitializer.initEffects(effects)

        setContent {
            ComposeBasicAppTheme {
                AppUI(this)
            }
        }

    }

    // Main App UI
    @Composable
    fun AppUI(context: Context) {

        categoryIdx = remember { mutableStateOf(0) }
        effectIdx = remember { mutableStateOf(0) }
        controlIdx = remember { mutableStateOf(0) }

        pageIdx = remember { mutableStateOf(0) }

        val defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        this.renderController.inputBitmap = remember { mutableStateOf(defaultBitmap) }
        this.renderController.outputBitmap = remember { mutableStateOf(defaultBitmap) }
        this.renderController.previewBitmaps =
            remember { mutableStateListOf(*Array(10) { defaultBitmap }) }

        //this.renderController.parameters = remember { mutableStateListOf(*Array(4) { 0f }) }

        showControls = remember { mutableStateOf(false) }
        showDefaultScreen = remember { mutableStateOf(true) }


        // Background ====================================
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {}


        // App UI ========================================
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if (showDefaultScreen.value) {

                // Welcome screen
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    TextWithShadow(
                        text = "Welcome to GlitchIO! ",
                        textAlign = TextAlign.Center,
                        color = LightFont,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextWithShadow(
                        text = "Click here to select an image.",
                        textAlign = TextAlign.Center,
                        color = LightFont,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.size(180.dp, 45.dp)
                    ) {
                        OpenButton()
                    }
                }
            } else {

                // Top bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    TopBar()
                }

                // Display Area
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1.0f)
                    ) {
                        Image(
                            bitmap = this@MainActivity.renderController.outputBitmap.value.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(160.dp))
                }


                // Bottom Layout ========================================
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(145.dp)
                ) {
                    AnimatedVisibility(visible = !showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {

                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(DarkGray)
                        ) {

                            // Effects Row
                            Box(
                                Modifier
                                    .offset(0.dp, -40.dp)
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                EffectsRow()
                            }

                            // Categories Row
                            Box(
                                Modifier
                                    .offset(0.dp, 0.dp)
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                CategoriesRow()
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = showControls.value,
                    enter = slideInVertically { height -> height * 2 } + fadeIn(),
                    exit = slideOutVertically { height -> height * 2 } + fadeOut()) {
                    ControlsUI()
                }
            }
        }
    }


    @Composable
    fun TopBar() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = DarkGray) {
            }
            Box(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                OpenButton()
            }
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                SaveButton()
            }

        }
    }


    @Composable
    fun OpenButton() {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? -> imageUri = uri }

        Box(
            modifier = Modifier
                .size(if (showDefaultScreen.value) 180.dp else 80.dp, 80.dp)
                .padding(0.dp)
        ) {
            TextButton(
                onClick = { launcher.launch("image/*") },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (showDefaultScreen.value) DarkGray else Color(0, 0, 0, 0),
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
            )
            {
                TextWithShadow(
                    text = if (showDefaultScreen.value) "Open Image" else "Open",
                    textAlign = TextAlign.Left,
                    fontSize = if (showDefaultScreen.value) 20.sp else 16.sp,
                    fontWeight = FontWeight.Light,
                    color = MidLightFont
                )
                imageUri?.let {
                    val bitmapFromUri = getBitmapFromUri(it)
                    val resizedBitmap = resizeBitmap(bitmapFromUri)

                    this@MainActivity.renderController.inputBitmap.value = resizedBitmap
                    this@MainActivity.renderController.outputBitmap.value = resizedBitmap
                    showDefaultScreen.value = false
                    this@MainActivity.renderController.renderPreviews()
                    inputImageUri = it
                    imageUri = null
                }
            }
        }

    }


    @Composable
    fun SaveButton() {
        Box(
            modifier = Modifier
                .size(80.dp, 40.dp)
                .padding(0.dp)
        ) {
            TextButton(
                onClick = {
                    saveBitmap(inputImageUri!!)
                },
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
            )
            {

                TextWithShadow(
                    text = "Save",
                    fontSize = 16.sp,
                    color = MidLightFont,
                    fontWeight = FontWeight.Light
                )

            }

        }

    }

    @Composable
    fun EffectCard(categoryIdx: Int, idx: Int) {
        val size = 80.dp

        Box(modifier = Modifier
            .width(size)
            .height(size)
            .clickable {
                onClickEffectCard(idx)
            }) {

            Surface(
                Modifier
                    .fillMaxSize(),
                color = if (effectIdx.value == idx) MidGray else DarkGray
            ) {}

            Image(
                bitmap = this@MainActivity.renderController.previewBitmaps[idx].asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .shadow(elevation = 4.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(25.dp)
            ) {
                TextWithShadow(
                    text = categories[categoryIdx].effects[idx].name,
                    fontSize = 14.sp,
                    color = LightFont,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraLight,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp, 2.dp)
                )
            }


        }

    }

    @Composable
    fun EffectsRow() {
        if (!showControls.value) {
            //this.renderController.renderPreviews()
        }

        this.renderController.renderPreviews()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                for (k in 0 until categories[categoryIdx.value].effects.size) {
                    Spacer(modifier = Modifier.width(10.dp))
                    EffectCard(categoryIdx.value, k)
                }
            }
        }
    }


    @Composable
    fun CategoriesRow() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(Modifier.align(Center)) {
                for (i in 0..categories.size - 1) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(Modifier
                        .clickable { categoryIdx.value = i }
                        .weight(1f))
                    {
                        TextWithShadow(
                            text = categories[i].name,
                            fontSize = 14.sp,
                            color = if (categoryIdx.value == i) LightFont else MidFont,
                            modifier = Modifier.offset(0.dp, 0.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }

    fun onClickEffectCard(idx: Int) {
        effectIdx.value = idx
        showControls.value = true

        for (i in currEffect.controls.indices) {
            val control = currEffect.controls[i]
            parameters[i] = control.default
        }

        renderController.renderEffect()
    }

    fun getBitmapFromUri(uri: Uri): Bitmap {
        val tempImageView = ImageView(this@MainActivity)
        tempImageView.setImageURI(uri)
        return (tempImageView.drawable as BitmapDrawable).bitmap
    }

    fun saveBitmap(uri: Uri) {
        val bitmap = this.renderController.outputBitmap.value

        val directory =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "")
        directory.mkdirs()

        var idx = 1
        val outputFileName = File(uri.path!!).nameWithoutExtension + "_glitched"
        var outputFile = File(directory, "$outputFileName.jpg")

        while (outputFile.exists()) {
            outputFile = File(directory, "$outputFileName$idx.jpg")
            idx++
        }

        Log.i("Saved to: ", outputFile.absolutePath)

        val fileOutputStream = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush();
        fileOutputStream.close();

    }
}


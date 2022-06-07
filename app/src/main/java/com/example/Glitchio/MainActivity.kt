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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.renderer.Renderer
import com.example.Glitchio.ui.theme.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream


// GL ============================
lateinit var parameters : MutableList<Float>

// Coroutines =====================================
val scope = CoroutineScope(Job() + Dispatchers.Main)

// UI =====================================
lateinit var categoryIdx : MutableState<Int>
lateinit var effectIdx : MutableState<Int>
lateinit var controlIdx : MutableState<Int>
lateinit var showControls : MutableState<Boolean>
lateinit var showDefaultScreen : MutableState<Boolean>
var isRendering  = false

val currCategory : Category get() = categories[categoryIdx.value]
val currEffect : Effect get() = currCategory.effects[effectIdx.value]
val currControl : Control get() = if (controlIdx.value < currEffect.controls.size) currEffect.controls[controlIdx.value] else currEffect.controls[0]

var inputImageUri : Uri? = null

// Bitmaps =====================================
lateinit var inputBitmap : MutableState<Bitmap>
lateinit var outputBitmap : MutableState<Bitmap>
lateinit var previewBitmaps : MutableList<Bitmap>


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeBasicAppTheme {
                AppUI(this)
            }
        }

    }


    @Composable
    fun AppUI(context: Context) {

        categoryIdx = remember { mutableStateOf(0) }
        effectIdx = remember { mutableStateOf(0) }
        controlIdx = remember { mutableStateOf(0) }

        val defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        inputBitmap = remember { mutableStateOf(defaultBitmap) }
        outputBitmap = remember { mutableStateOf(defaultBitmap) }
        previewBitmaps = remember { mutableStateListOf(*Array(10){defaultBitmap})}
        parameters = remember { mutableStateListOf(*Array(4){0f})}

        showControls = remember { mutableStateOf(false) }
        showDefaultScreen = remember { mutableStateOf(true) }


        // Background ====================================
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {}

        // App UI ========================================
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if(showDefaultScreen.value) {

                // Welcome screen
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    TextWithShadow(
                        text = "Welcome to GlitchIO! ",
                        textAlign = TextAlign.Center,
                        color = MidFont,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextWithShadow(
                        text = "Click here to select an image.",
                        textAlign = TextAlign.Center,
                        color = DarkFont,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.size(180.dp, 45.dp)
                    ) {
                        OpenButton()
                    }
                }
            }
            else {

                // Top bar
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                ){
                    TopBar()
                }

                // Display Area
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Box(modifier = Modifier
                            .fillMaxSize()
                            .weight(1.0f)
                    ) {
                        Image(
                            bitmap = outputBitmap.value.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(160.dp))
                }

                // Effect selection / controls
                Box(modifier = Modifier.align(Alignment.BottomCenter)
                ){
                    AnimatedVisibility(visible = !showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {
                        Column() {
                            EffectsRow()
                            CategoriesRow()
                        }
                    }

                    AnimatedVisibility(visible = showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {
                        ControlsLayout(context)
                    }

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
        ){
            Box(modifier = Modifier.align(Alignment.TopStart)
            ) {
                OpenButton()
            }
            Box(modifier = Modifier.align(Alignment.TopEnd)
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
                    backgroundColor = if (showDefaultScreen.value) DarkPurple else Color(0x000F0E13),
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
                    color = MidFont
                )
                imageUri?.let {
                    inputBitmap.value = getBitmapFromUri(it)
                    outputBitmap.value = inputBitmap.value
                    showDefaultScreen.value = false
                    renderPreviews()
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
                    color = MidFont
                )

            }

        }

    }


    @Composable
    fun EffectsRow() {
        if(!showControls.value) {
            renderPreviews()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clipToBounds()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f), color = PurpleGrey40
            ) {}
            for (i in categories.indices) {
                AnimatedVisibility(
                    visible = categoryIdx.value == i,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {


                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                    ) {

                        for (k in 0 until categories[i].effects.size) {
                            EffectCard(i, k)
                        }


                    }


                }
            }

        }
    }

    @Composable
    fun EffectCard(categoryIdx: Int, effectidx: Int) {

        Box(modifier = Modifier
            .width(110.dp)
            .height(120.dp)
            .padding(5.dp, 10.dp)
            .shadow(elevation = 4.dp)
            .clickable {
                effectIdx.value = effectidx
                showControls.value = true
                for (i in currEffect.controls.indices) {
                    val c = currEffect.controls[i]
                    parameters[i] = c.default
                }
                requestRender(parameters)

            }) {
            Surface(
                color = PurpleGrey40,
                modifier = Modifier
                    .fillMaxSize()

            ) {}

            Image(
                bitmap = previewBitmaps[effectidx].asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                Surface(
                    color = DarkTint,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f)
                ) {}

                TextWithShadow(
                    text = categories[categoryIdx].effects[effectidx].name,
                    fontSize = 12.sp,
                    color = LightFont,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp, 2.dp)
                )

            }

        }

    }


    @Composable
    fun CategoriesRow() {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .shadow(1.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f), color = PurpleGrey
            ) {}
            Row() {
                for (i in 0..categories.size - 1) {

                    Box(modifier = Modifier.weight(1f)) {

                        if (categoryIdx.value == i) {
                            Divider(
                                color = LightFont, modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .align(Alignment.BottomCenter)
                            )
                            Surface(modifier = Modifier.fillMaxSize().alpha(0.05f), color = LightFont, ){}
                        }

                        TextButton(
                            onClick = { categoryIdx.value = i },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        {
                            TextWithShadow(
                                text = categories[i].name,
                                fontSize = 15.sp,
                                color = if (categoryIdx.value == i) LightFont else MidFont
                            )

                        }

                    }

                }
            }
        }

    }

    fun requestRender(params : List<Float>) {

        if(!isRendering) {
            scope.launch {
                withContext(Dispatchers.Default) {
                    isRendering = true

                    val rendererClass = categories[categoryIdx.value].effects[effectIdx.value].renderer.java
                    val renderer = rendererClass.constructors.first().newInstance(this@MainActivity) as Renderer
                    val renderedBitmap = renderer.render(inputBitmap.value, params)

                    withContext(Dispatchers.Main) {
                        outputBitmap.value = renderedBitmap
                        isRendering = false
                    }
                }
            }
        }

    }

    fun renderPreviews(){
        for (i in 0 until currCategory.effects.size) {
            val effect = currCategory.effects[i]
            val params = List(effect.controls.size) { k -> effect.controls[k].default }
            renderPreviewBitmap(params, categoryIdx.value, i)
        }

    }
    fun renderPreviewBitmap(params: List<Float>, categoryIdx: Int, effectIdx: Int) {

        scope.launch {
            withContext(Dispatchers.Default) {

                val rendererClass = categories[categoryIdx].effects[effectIdx].renderer.java
                val renderer =
                    rendererClass.constructors.first().newInstance(this@MainActivity) as Renderer

                val renderedBitmap = renderer.render(inputBitmap.value, params)

                withContext(Dispatchers.Main) {
                    previewBitmaps[effectIdx] = renderedBitmap
                }
            }
        }

    }



    fun getBitmapFromUri(uri : Uri) : Bitmap{
        val tempImageView = ImageView(this@MainActivity)
        tempImageView.setImageURI(uri)
        return (tempImageView.drawable as BitmapDrawable).bitmap
    }

    fun saveBitmap(uri: Uri) {
        val bitmap = outputBitmap.value

        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "")
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


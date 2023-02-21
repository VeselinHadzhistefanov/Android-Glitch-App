package com.example.Glitchio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Base64
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Glitchio.components.Category
import com.example.Glitchio.components.Control
import com.example.Glitchio.components.Effect
import com.example.Glitchio.components.categories
import com.example.Glitchio.renderer.HueShift
import com.example.Glitchio.renderer.Renderer
import com.example.Glitchio.ui.theme.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sin


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

val effectCardHeight = 125.dp



class MainActivity : ComponentActivity() {

    lateinit var effectController : EffectController


    // OnCreate - App UI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        effectController = EffectController(this)

        setContent {
            ComposeBasicAppTheme {
                AppUI(this)
            }
        }

    }

    // App UI


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
        ){}



        // App UI ========================================
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if(showDefaultScreen.value) {

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


                // Bottom Layout ========================================

                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    AnimatedVisibility(visible = !showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {

                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            // Background
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = DarkGray
                            ) {}

                            // Top edge
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp),
                                color = MidDarkGray
                            ) {}

                            // Effects Row
                            Box(
                                Modifier
                                    .offset(0.dp, -50.dp)
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
                                    .height(30.dp)
                            ) {

                                CategoriesRow()
                            }


                        }
                    }
                }

                // Effect Controls
                val height = currEffect.controls.size * 45 + 15
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(height.dp))

                {
                    AnimatedVisibility(visible = showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {

                        // Background
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(0.8f),
                            color = DarkGray
                        ) {}

                        ControlsLayout(context)

                        // Top edge
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp),
                            color = MidDarkGray
                        ) {}
                    }
                }


                /*
                // Effect selection / controls
                Box(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(190.dp)
                ){
                    AnimatedVisibility(visible = !showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = DarkGray
                        ){}

                        Box(){
                            //CategoriesRow()
                        }
                        Box(){
                            //EffectsRow()
                        }

                    }

                    AnimatedVisibility(visible = showControls.value,
                        enter = slideInVertically { height -> height * 2 } + fadeIn(),
                        exit = slideOutVertically { height -> height * 2 } + fadeOut()) {

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = DarkGray
                        ){}

                        ControlsLayout(context)
                    }

                }
                */

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
            Surface(modifier = Modifier.fillMaxSize(), color = DarkGray) {
            }
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
                    backgroundColor = if (showDefaultScreen.value) DarkGray else Color(0,0,0,0),
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

                    inputBitmap.value = resizedBitmap
                    outputBitmap.value = resizedBitmap
                    showDefaultScreen.value = false
                    effectController.renderPreviews()
                    inputImageUri = it
                    imageUri = null
                }
            }
        }

    }
    fun resizeBitmap(bitmap : Bitmap) : Bitmap{
        var x = bitmap.width
        var y = bitmap.height
        val max = 500

        if(x > y ){
            x = x * max/y
            y = max
        }
        else{
            y = y * max/x
            x = max
        }

        val newBitmap = Bitmap.createScaledBitmap(bitmap, x, y, false)

        return newBitmap
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
    fun EffectCard(categoryIdx: Int, effectidx: Int) {
        val size = 80.dp

        Box(modifier = Modifier
            .width(size)
            .height(size)
            .clickable {
                effectIdx.value = effectidx
                showControls.value = true
                for (i in currEffect.controls.indices) {
                    val c = currEffect.controls[i]
                    parameters[i] = c.default
                }
                effectController.requestRender(parameters)
                effectController.rendererCreated = false
            }) {

            Surface(Modifier
                .fillMaxSize(),
            color = if (effectIdx.value == effectidx) MidGray else DarkGray) {}

            Image(
                bitmap = previewBitmaps[effectidx].asImageBitmap(),
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
                    .height(25.dp)) {
                    TextWithShadow(
                        text = categories[categoryIdx].effects[effectidx].name,
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
        if(!showControls.value) {
            effectController.renderPreviews()
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
        ) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())
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
        Box(modifier = Modifier.fillMaxSize()
        ) {
            Row() {
                for (i in 0..categories.size - 1) {

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(Modifier.clickable { categoryIdx.value = i }) {
                        TextWithShadow(
                            text = categories[i].name,
                            fontSize = 14.sp,
                            color = if (categoryIdx.value == i) LightFont else MidFont,
                            modifier = Modifier.offset(0.dp, -5.dp)
                        )
                    }
                    
                }
            }

        }
    }

    var timeFrame : Double = 0.0
    fun setRenderParameters(params: List<Float>, movementParams : List<Float> = arrayListOf<Float>()){
        val handler: Handler = Handler()
        val delay = 200L
        handler.postDelayed(object : java.lang.Runnable {
            override fun run() {
                val newParams  = arrayListOf<Float>()
                val magnitude = 0.1f

                for (parameter in params){
                    newParams.add(parameter + sin(timeFrame).toFloat() * magnitude)
                }
                timeFrame = (timeFrame+0.01)%(Math.PI*2)


                effectController.requestRender(newParams)
                handler.postDelayed(this, delay)
            }
        }, delay)
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


package com.example.Glitchio

import android.Manifest
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.Glitchio.components.ControlsLayout
import com.example.Glitchio.components_legacy.TextWithShadow
import com.example.Glitchio.effects.initEffects
import com.example.Glitchio.ui.theme.*
import java.io.File
import java.io.FileOutputStream


lateinit var categoryIdx: MutableState<Int>
lateinit var effectIdx: MutableState<Int>
lateinit var controlIdx: MutableState<Int>
lateinit var showControls: MutableState<Boolean>
lateinit var showWelcomeScreen: MutableState<Boolean>
var isRendering = false
var inputImageUri: Uri? = null

lateinit var renderController: RenderController
lateinit var pageIdx: MutableState<Int>

val TOP_BAR_HEIGHT = 50.dp
val TOP_BAR_SPACING = 20.dp
val TOP_BAR_ICON_SIZE = 24.dp

class MainActivity : ComponentActivity() {

    init {
        renderController = RenderController(this)
    }

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

        initEffects()

        setContent {
            ComposeBasicAppTheme {
                AppUI()
            }
        }

    }

    // Main App UI
    @Composable
    fun AppUI() {

        categoryIdx = remember { mutableStateOf(0) }
        effectIdx = remember { mutableStateOf(0) }
        controlIdx = remember { mutableStateOf(0) }

        pageIdx = remember { mutableStateOf(0) }

        val defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        inputBitmap = remember { mutableStateOf(defaultBitmap) }
        outputBitmap = remember { mutableStateOf(defaultBitmap) }
        previewBitmaps = remember { mutableStateListOf(*Array(10) { defaultBitmap }) }

        //this.renderController.parameters = remember { mutableStateListOf(*Array(4) { 0f }) }

        showControls = remember { mutableStateOf(false) }
        showWelcomeScreen = remember { mutableStateOf(true) }

        // Background ====================================
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {}


        // App UI ========================================
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if (showWelcomeScreen.value) {
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
                        OpenButtonWelcomeScreen()
                    }
                }
            } else {

                // Display Area
                Column() {
                    TopBar()
                    Box(
                        modifier = Modifier.weight(1.0f)
                    ) {
                        DisplayArea()
                    }
                    Spacer(modifier = Modifier.height(180.dp))
                }

                EffectSelectionLayout()
                ControlsLayout()
            }
        }
    }

    @Composable
    fun TopBar() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(TOP_BAR_HEIGHT)
                .background(DarkGray)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.Center)
            ) {
                Spacer(modifier = Modifier.width(TOP_BAR_SPACING))
                OpenButton()
                Spacer(modifier = Modifier.width(TOP_BAR_SPACING))
                RandomEffectButton()
                Spacer(modifier = Modifier.weight(1.0f))
                SaveButton()
                Spacer(modifier = Modifier.width(TOP_BAR_SPACING))
            }
        }
    }

    @Composable
    fun OpenButton() {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? -> imageUri = uri }

        Icon(
            modifier = Modifier
                .size(TOP_BAR_ICON_SIZE)
                .clickable {
                    launcher.launch("image/*")
                },
            painter = painterResource(id = R.drawable.folder),
            contentDescription = null,
            tint = IconLargeLight
        )
        imageUri?.let {
            openImageFromUri(it)
        }
    }

    @Composable
    fun OpenButtonWelcomeScreen() {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? -> imageUri = uri }

        Box(
            modifier = Modifier.size(180.dp, 80.dp)
        ) {
            TextButton(
                onClick = {
                    launcher.launch("image/*")
                },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = DarkGray,
                ),
                modifier = Modifier.fillMaxSize()
            )
            {
                TextWithShadow(
                    text = "Open Image",
                    textAlign = TextAlign.Left,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = MidLightFont
                )
            }
        }
        imageUri?.let {
            openImageFromUri(it)
        }
    }

    private fun openImageFromUri(uri: Uri) {
        val bitmap = getBitmapFromUri(uri)
        val resizedBitmap = resizeBitmap(bitmap)

        inputImageUri = uri
        inputBitmap.value = resizedBitmap
        outputBitmap.value = resizedBitmap
        renderController.renderPreviews()
        showWelcomeScreen.value = false
    }

    @Composable
    fun SaveButton() {
        Icon(
            modifier = Modifier
                .size(TOP_BAR_ICON_SIZE)
                .clickable {
                    saveBitmap(inputImageUri!!)
                },
            painter = painterResource(id = R.drawable.diskette),
            contentDescription = null,
            tint = IconLargeLight
        )
    }

    @Composable
    fun RandomEffectButton() {
        Icon(
            modifier = Modifier
                .size(TOP_BAR_ICON_SIZE)
                .clickable {
                    onClickRandomEffect()
                },
            painter = painterResource(id = R.drawable.random__1_),
            contentDescription = null,
            tint = IconLargeLight
        )
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val tempImageView = ImageView(this@MainActivity)
        tempImageView.setImageURI(uri)
        return (tempImageView.drawable as BitmapDrawable).bitmap
    }

    private fun saveBitmap(uri: Uri) {
        val bitmap = outputBitmap.value

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100
        )

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


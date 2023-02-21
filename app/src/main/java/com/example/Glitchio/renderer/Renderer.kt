package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.*
import android.util.Log
import com.example.Glitchio.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


abstract class Renderer(val context: Context) {
    var width = 0
    var height = 0

    var mProgram = 0
    var mFramebuffer = 0
    val mTextures = IntArray(2)

    private var mVertexPositionHandle = 0
    private var mTextureCoordinateHandle = 0
    lateinit var mVertexBuffer: FloatBuffer

    lateinit var eglDisplay : EGLDisplay
    lateinit var eglSurface : EGLSurface
    lateinit var eglContext : EGLContext


    private val mTriangleVertices = floatArrayOf(
        -1.0f, -1.0f, 0f, 0f,
        0f, 1.0f, -1.0f, 0f,
        1f, 0f, -1.0f, 1.0f, 0f, 0f, 1f, 1.0f,
        1.0f, 0f, 1f, 1f
    )

    // Render function implemented by each effect
    abstract fun render(inputBitmap: Bitmap, parameters : Array<Float>) : Bitmap

    fun initProgram(vertexShaderPath: Int, fragmentShaderPath: Int) {
        // Create EGL context
        createContext()
        // Create an object to hold information from rendering
        createFramebuffer()
        // Create an object to holding vertices
        mVertexBuffer = ByteBuffer.allocateDirect(mTriangleVertices.size * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertexBuffer.put(mTriangleVertices).position(0)

        // Create shader program
        val vertexShader = readTextFile(vertexShaderPath)
        val fragmentShader = readTextFile(fragmentShaderPath)
        mProgram = createProgram(vertexShader, fragmentShader)
        GLES20.glUseProgram(mProgram)

        // Get handles for vertex positions and texture UV coordinates
        mVertexPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position")
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate")


        // Allocate vertex position data
        mVertexBuffer.position(TRIANGLE_VERTICES_DATA_POS_OFFSET)
        GLES20.glVertexAttribPointer(mVertexPositionHandle, 3, GLES20.GL_FLOAT, false,
            TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mVertexBuffer)
        getGLError(9)
        GLES20.glEnableVertexAttribArray(mVertexPositionHandle)
        getGLError(10)

        // Allocate texture UV data
        mVertexBuffer.position(TRIANGLE_VERTICES_DATA_UV_OFFSET)
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 3, GLES20.GL_FLOAT, false,
            TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mVertexBuffer)
        getGLError(11)

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle)
        getGLError(12)
    }

    private fun createContext() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val vers = IntArray(2)
        EGL14.eglInitialize(eglDisplay, vers, 0, vers, 1)
        val configAttr = intArrayOf(
            EGL14.EGL_COLOR_BUFFER_TYPE, EGL14.EGL_RGB_BUFFER,
            EGL14.EGL_LEVEL, 0,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfig = IntArray(1)
        EGL14.eglChooseConfig(
            eglDisplay, configAttr, 0,
            configs, 0, 1, numConfig, 0
        )
        val config = configs[0]
        val surfAttr = intArrayOf(
            EGL14.EGL_WIDTH, width,
            EGL14.EGL_HEIGHT, height,
            EGL14.EGL_NONE
        )
        eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, config, surfAttr, 0)
        val ctxAttrib = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )
        eglContext = EGL14.eglCreateContext(eglDisplay, config, EGL14.EGL_NO_CONTEXT, ctxAttrib, 0)
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    fun destroyContext(){
        EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglTerminate(eglDisplay)
    }

    private fun createProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        val programHandle = GLES20.glCreateProgram()

        GLES20.glAttachShader(programHandle, vertexShader)
        GLES20.glAttachShader(programHandle, fragmentShader)

        GLES20.glLinkProgram(programHandle)

        return programHandle
    }

    private fun loadShader(shaderType: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(shaderType)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)


        return shader
    }


    private fun createFramebuffer() {
        val bufferId = IntArray(1)
        GLES20.glGenFramebuffers(1, bufferId, 0)
        mFramebuffer = bufferId[0]
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffer)
    }

    fun initTextures(sourceBitmap: Bitmap) {
        width = sourceBitmap.width
        height = sourceBitmap.height

        GLES20.glGenTextures(2, mTextures, 0)
        for (i in 0..1){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i])
            GLES20.glEnable(GLES10.GL_MULTISAMPLE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, sourceBitmap, GLES20.GL_UNSIGNED_BYTE, 0)
        }

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0])

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[1])

        // Set viewport
        GLES20.glViewport(0, 0, width, height)


    }


    private fun readTextFile(resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        var nextLine: String?
        val content = StringBuilder()
        while (bufferedReader.readLine().also { nextLine = it } != null) {
            content.append(nextLine)
            content.append('\n')
        }
        return content.toString()
    }


    fun getOutputBitmap() : Bitmap{
        val buffer = ByteBuffer.allocate(width * height * 4)
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }



    companion object {
        private val FLOAT_SIZE_BYTES = 4
        val TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES
        val TRIANGLE_VERTICES_DATA_POS_OFFSET = 0
        val TRIANGLE_VERTICES_DATA_UV_OFFSET = 3
    }


    fun getGLError(msg : String = ""){
        Log.i("GL ERROR: $msg ", GLES20.glGetError().toString())
        GLES20.glGetError()
    }
    fun getGLError(msg : Int){
        getGLError(msg.toString())
    }

    public class Timer(){
        var startTime = System.currentTimeMillis()
        fun print(msg : String = ""){
            val t = System.currentTimeMillis() - startTime
            Log.i("$msg ", t.toString())
            startTime = System.currentTimeMillis()
        }
    }


}
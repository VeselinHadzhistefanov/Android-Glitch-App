package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import com.example.Glitchio.R

class Wave(context : Context) : Renderer(context) {

    private val vertexShaderPath = R.raw.vertex_shader
    private val fragmentShaderPath = R.raw.wave

    init {
        initProgram(vertexShaderPath, fragmentShaderPath)
    }

    override fun render(inputBitmap: Bitmap, parameters : Array<Float>): Bitmap {
        GLES20.glGetError()

        // Initialize shaders and load texture
        initTextures(inputBitmap)

        // Parameter uniforms
        val paramFloat1Handle = GLES20.glGetUniformLocation(mProgram, "paramFloat1")
        val paramFloat2Handle = GLES20.glGetUniformLocation(mProgram, "paramFloat2")

        // Set uniforms
        GLES20.glUniform1f(paramFloat1Handle, parameters[0])
        GLES20.glUniform1f(paramFloat2Handle, parameters[1])

        // Draw
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 0)
        GLES20.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[0], 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glGetError()


        GLES20.glFinish()
        val outputBitmap = getOutputBitmap()
        return outputBitmap
    }

}
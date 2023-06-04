package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import com.example.Glitchio.R
import com.example.Glitchio.inputBitmap
import com.example.Glitchio.parameters

class Noise(context : Context) : Renderer(context) {

    init {
        initShaderProgram(vertexShaderPath, R.raw.noise)
    }

    override fun render(): Bitmap {
        initTextures(inputBitmap.value)
        setUniformValues(parameters[0], parameters[1])

        //Run shaders
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 0)
        GLES20.glFramebufferTexture2D(
            GLES30.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            mTextures[1],
            0
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glFinish()
        return getOutputBitmap()
    }
}
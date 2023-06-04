package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import com.example.Glitchio.R
import com.example.Glitchio.inputBitmap
import com.example.Glitchio.parameters

class Sort(context : Context) : Renderer(context){

    init {
        initShaderProgram(vertexShaderPath, R.raw.sort)
    }

    override fun render(): Bitmap {
        initTextures(inputBitmap.value)
        setUniformValues(parameters[0], parameters[1])

        // Create resolution and frame uniforms
        val iResolutionHandle = GLES20.glGetUniformLocation(mProgram, "iResolution")
        val iFrameHandle = GLES20.glGetUniformLocation(mProgram, "iFrame")

        // Set framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffer)

        // Set viewport
        GLES20.glViewport(0, 0, width, height)

        //Set resolution uniforms
        GLES20.glUniform2f(iResolutionHandle, width.toFloat(), height.toFloat())

        // Sort pixels
        for (i in 0..width / 2) {
            GLES20.glUniform1i(iFrameHandle, i * 2)
            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 0)
            GLES20.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                mTextures[1],
                0
            )
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            GLES20.glUniform1i(iFrameHandle, i * 2 + 1)
            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 1)
            GLES20.glFramebufferTexture2D(
                GLES30.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                mTextures[0],
                0
            )
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        }

        GLES20.glFinish()
        return getOutputBitmap()
    }
}
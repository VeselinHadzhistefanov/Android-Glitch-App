package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import com.example.Glitchio.R

class Sort(context : Context) : Renderer(context, "Sort", "Amount", "Direction"){

    private val vertexShaderPath = R.raw.vertex_shader
    private val fragmentShaderPath = R.raw.sort

    init {
        initProgram(vertexShaderPath, fragmentShaderPath)
    }

    override fun render(inputBitmap: Bitmap, parameters : Array<Float>): Bitmap {

        val t = Timer()
        t.print("Start")

        // Initialize shaders and load texture
        initTextures(inputBitmap)

        // Parameter uniforms
        val paramFloat1Handle = GLES20.glGetUniformLocation(mProgram, "paramFloat1")
        val paramFloat2Handle = GLES20.glGetUniformLocation(mProgram, "paramFloat2")

        // Effect uniforms
        val iResolutionHandle = GLES20.glGetUniformLocation(mProgram, "iResolution")
        val iFrameHandle = GLES20.glGetUniformLocation(mProgram, "iFrame")

        // Set framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffer)

        // Set viewport
        GLES20.glViewport(0, 0, width, height)

        // Set uniforms
        GLES20.glUniform1f(paramFloat1Handle, parameters[0])
        GLES20.glUniform1f(paramFloat2Handle, parameters[1])
        GLES20.glUniform2f(iResolutionHandle, width.toFloat(), height.toFloat())

        t.print("Init")
        // Sort pixels
        for (i in 0..width/2){

            GLES20.glUniform1i(iFrameHandle, i*2)

            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 0)
            GLES20.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[1], 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            GLES20.glUniform1i(iFrameHandle, i*2+1)

            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 1)
            GLES20.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[0], 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        }
        t.print("Draw")

        GLES20.glFinish()
        val outputBitmap = getOutputBitmap()
        t.print("Finish")
        return outputBitmap
    }


}
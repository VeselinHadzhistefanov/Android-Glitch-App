package com.example.Glitchio.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import com.example.Glitchio.R


class Blur(context : Context) : Renderer(context) {

    private val vertexShaderPath = R.raw.vertex_shader
    private val fragmentShaderPath = R.raw.blur


    override fun render(inputBitmap: Bitmap, parameters : List<Float>): Bitmap {

        // Initialize shaders and load texture
        initProgram(vertexShaderPath, fragmentShaderPath)
        initTextures(inputBitmap)

        // Create parameter uniforms
        val paramFloat1Handle = GLES20.glGetUniformLocation(mProgram, "paramFloat1")

        // Set parameter values
        GLES20.glUniform1f(paramFloat1Handle, parameters[0])

        // Blur
        for (i in 0..10){

            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 0)
            GLES20.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[1], 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)


            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "u_Texture"), 1)
            GLES20.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextures[0], 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        }


        GLES20.glFinish()
        val outputBitmap = getOutputBitmap()
        destroyContext()
        return outputBitmap
    }

}
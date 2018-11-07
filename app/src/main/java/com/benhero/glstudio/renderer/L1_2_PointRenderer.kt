package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 基础概念 + 点 的绘制
 *
 * @author Benhero
 */
class L1_2_PointRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = "" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = a_Position;\n" +
                "    gl_PointSize = 40.0;\n" +
                "}"
        private val FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "uniform vec4 u_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = u_Color;\n" +
                "}"
        private val POINT_DATA = floatArrayOf(0f, 0f)
        private val POSITION_COMPONENT_COUNT = 2
    }
    private val vertexData: FloatBuffer
    private var uColorLocation: Int = 0

    init {
        vertexData = BufferUtil.createFloatBuffer(POINT_DATA)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        val aPositionLocation = getAttrib("a_Position")
        uColorLocation = getUniform("u_Color")

        vertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUniform4f(uColorLocation, 1.0f, 0f, 0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }
}
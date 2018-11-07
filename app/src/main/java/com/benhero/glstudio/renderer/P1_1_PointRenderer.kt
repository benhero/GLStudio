package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 动态改变顶点位置 & 颜色
 *
 * @author Benhero
 */
class P1_1_PointRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = "" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = a_Position;\n" +
                "    gl_PointSize = 100.0;\n" +
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

    private val mVertexData: FloatBuffer
    private var uColorLocation: Int = 0

    private val mRandom = Random()

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        val aPositionLocation = getAttrib("a_Position")
        uColorLocation = getUniform("u_Color")

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // 只要持有传递给GL层的Buffer引用，就可以动态改变相关的数据信息
        mVertexData.put(floatArrayOf(0.9f * mRandom.nextFloat() * (if (mRandom.nextFloat() > 0.5f) 1 else -1).toFloat(), 0.9f * mRandom.nextFloat() * (if (mRandom.nextFloat() > 0.5f) 1 else -1).toFloat()))
        mVertexData.position(0)

        GLES20.glUniform4f(uColorLocation, mRandom.nextFloat(), mRandom.nextFloat(), mRandom.nextFloat(), 1.0f)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }
}
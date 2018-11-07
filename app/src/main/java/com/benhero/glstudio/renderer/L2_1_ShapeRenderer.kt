/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 图形
 *
 * @author Benhero
 */
class L2_1_ShapeRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        /**
         * 顶点着色器：之后定义的每个都会传1次给顶点着色器
         */
        private val VERTEX_SHADER = "" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = a_Position;\n" +
                "    gl_PointSize = 30.0;\n" +
                "}"
        /**
         * 片段着色器
         */
        private val FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "uniform vec4 u_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = u_Color;\n" +
                "}"
        /**
         * 顶点数据数组
         */
        private val POINT_DATA = floatArrayOf(
                // 两个点的x,y坐标（x，y各占1个分量）
                0f, 0f, 0f, 0.5f, -0.5f, 0f, 0f, 0f - 0.5f, 0.5f, 0f - 0.5f, 0.5f, 0.5f - 0.5f)
        private val POSITION_COMPONENT_COUNT = 2
        private val DRAW_COUNT = POINT_DATA.size / POSITION_COMPONENT_COUNT
    }
    private val vertexData: FloatBuffer
    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var drawIndex = 0

    init {
        vertexData = BufferUtil.createFloatBuffer(POINT_DATA)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        uColorLocation = getUniform("u_Color")
        aPositionLocation = getAttrib("a_Position")

        vertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        drawIndex++
        // 几何图形相关定义：http://wiki.jikexueyuan.com/project/opengl-es-guide/basic-geometry-definition.html
        drawTriangle()
        drawLine()
        drawPoint()
        if (drawIndex >= DRAW_COUNT) {
            drawIndex = 0
        }
    }

    private fun drawPoint() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, drawIndex)
    }

    private fun drawLine() {
        // GL_LINES：每2个点构成一条线段
        // GL_LINE_LOOP：按顺序将所有的点连接起来，包括首位相连
        // GL_LINE_STRIP：按顺序将所有的点连接起来，不包括首位相连
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, drawIndex)
    }

    private fun drawTriangle() {
        // GL_TRIANGLES：每3个点构成一个三角形
        // GL_TRIANGLE_STRIP：相邻3个点构成一个三角形,不包括首位两个点
        // GL_TRIANGLE_FAN：第一个点和之后所有相邻的2个点构成一个三角形
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, drawIndex)
    }
}
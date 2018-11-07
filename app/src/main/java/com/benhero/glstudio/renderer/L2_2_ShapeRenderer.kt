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
 * 图形 - 多边形
 *
 * @author Benhero
 */
open class L2_2_ShapeRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        /**
         * 顶点着色器：之后定义的每个都会传1次给顶点着色器
         */
        private val VERTEX_SHADER = "" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = a_Position;\n" +
                "    gl_PointSize = 10.0;\n" +
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
        private val POSITION_COMPONENT_COUNT = 2
        /**
         * 多边形顶点与中心点的距离
         */
        private val RADIUS = 0.5f
        /**
         * 起始点的弧度
         */
        private val START_POINT_RADIAN = (2 * Math.PI / 4).toFloat()
    }

    private var mVertexData: FloatBuffer? = null
    private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    /**
     * 多边形的顶点数，即边数
     */
    private var mPolygonVertexCount = 3
    /**
     * 绘制所需要的顶点数
     */
    private lateinit var mPointData: FloatArray

    open val vertexShader: String
        get() = VERTEX_SHADER

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(vertexShader, FRAGMENT_SHADER)

        uColorLocation = getUniform("u_Color")
        aPositionLocation = getAttrib("a_Position")

        GLES20.glEnableVertexAttribArray(aPositionLocation)

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        updateVertexData()
        drawShape()
        drawLine()
        drawPoint()
        updatePolygonVertexCount()
    }

    private fun updateVertexData() {
        // 边数+中心点+闭合点；一个点包含x、y两个向量
        mPointData = FloatArray((mPolygonVertexCount + 2) * 2)

        // 组成多边形的每个三角形的中心点角的弧度
        val radian = (2 * Math.PI / mPolygonVertexCount).toFloat()
        // 中心点
        mPointData[0] = 0f
        mPointData[1] = 0f
        // 多边形的顶点数据
        for (i in 0 until mPolygonVertexCount) {
            mPointData[2 * i + 2] = (RADIUS * Math.cos((radian * i + START_POINT_RADIAN).toDouble())).toFloat()
            mPointData[2 * i + 1 + 2] = (RADIUS * Math.sin((radian * i + START_POINT_RADIAN).toDouble())).toFloat()
        }
        // 闭合点：与多边形的第一个顶点重叠
        mPointData[mPolygonVertexCount * 2 + 2] = (RADIUS * Math.cos(START_POINT_RADIAN.toDouble())).toFloat()
        mPointData[mPolygonVertexCount * 2 + 3] = (RADIUS * Math.sin(START_POINT_RADIAN.toDouble())).toFloat()

        mVertexData = BufferUtil.createFloatBuffer(mPointData!!)
        mVertexData!!.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData)
    }


    private fun drawShape() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mPolygonVertexCount + 2)
    }

    private fun drawPoint() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, mPolygonVertexCount + 2)
    }

    private fun drawLine() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 1, mPolygonVertexCount)
    }

    /**
     * 更新多边形的边数
     */
    private fun updatePolygonVertexCount() {
        mPolygonVertexCount++
        mPolygonVertexCount = if (mPolygonVertexCount > 32) 3 else mPolygonVertexCount
    }
}
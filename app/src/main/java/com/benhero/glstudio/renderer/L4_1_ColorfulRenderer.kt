package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.ProjectionMatrixHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 渐变色
 *
 * @author Benhero
 */
class L4_1_ColorfulRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = "" +
                "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                // a_Color：从外部传递进来的每个顶点的颜色值
                "attribute vec4 a_Color;\n" +
                // v_Color：将每个顶点的颜色值传递给片段着色器
                "varying vec4 v_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    v_Color = a_Color;\n" +
                "    gl_PointSize = 30.0;\n" +
                "    gl_Position = u_Matrix * a_Position;\n" +
                "}"
        private val FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                // v_Color：从顶点着色器传递过来的颜色值
                "varying vec4 v_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = v_Color;\n" +
                "}"

        private val POINT_DATA = floatArrayOf(-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f)
        private val COLOR_DATA = floatArrayOf(
                // 一个顶点有3个向量数据：r、g、b
                1f, 0.5f, 0.5f, 1f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 0f)


        /**
         * 坐标占用的向量个数
         */
        private val POSITION_COMPONENT_COUNT = 2
        /**
         * 颜色占用的向量个数
         */
        private val COLOR_COMPONENT_COUNT = 3
    }

    private val mVertexData: FloatBuffer
    private val mColorData: FloatBuffer
    private var mProjectionMatrixHelper: ProjectionMatrixHelper? = null

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mColorData = BufferUtil.createFloatBuffer(COLOR_DATA)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        val aPositionLocation = getAttrib("a_Position")
        val aColorLocation = getAttrib("a_Color")
        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation,
                POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        mColorData.position(0)
        GLES20.glVertexAttribPointer(aColorLocation,
                COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mColorData)
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mProjectionMatrixHelper!!.enable(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }
}

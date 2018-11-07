package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.ProjectionMatrixHelper
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 索引绘制
 *
 * @author Benhero
 */
class L5_IndexRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = "" +
                "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = u_Matrix * a_Position;\n" +
                "}"
        private val FRAGMENT_SHADER = "" +
                "precision mediump float;\n" +
                "uniform vec4 u_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = u_Color;\n" +
                "}"

        private val POINT_DATA = floatArrayOf(-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0f, -1.0f, 0f, 1.0f)

        /**
         * 数组绘制的索引:当前是绘制三角形，所以是3个元素构成一个绘制顺序
         */
        private val VERTEX_INDEX = shortArrayOf(0, 1, 2, 0, 2, 3, 0, 4, 1, 3, 2, 5)

        private val POSITION_COMPONENT_COUNT = 2
    }
    private val mVertexData: FloatBuffer
    /**
     * 顶点索引数据缓冲区：ShortBuff，占2位的Byte
     */
    private val mVertexIndexBuffer: ShortBuffer
    private var uColorLocation: Int = 0
    private var mProjectionMatrixHelper: ProjectionMatrixHelper? = null

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mVertexIndexBuffer = BufferUtil.createShortBuffer(VERTEX_INDEX)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        uColorLocation = getUniform("u_Color")
        val aPositionLocation = getAttrib("a_Position")
        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mProjectionMatrixHelper!!.enable(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)

        mVertexIndexBuffer.position(0)
        // 绘制相对复杂的图形时，若顶点有较多重复时，对比数据占用空间而言，glDrawElements会比glDrawArrays小很多，也会更高效
        // 因为在有重复顶点的情况下，glDrawArrays方式需要的3个顶点位置是用Float型的，占3*4的Byte值；
        // 而glDrawElements需要3个Short型的，占3*2Byte值
        // 1. 图形绘制方式； 2. 绘制的顶点数； 3. 索引的数据格式； 4. 索引的数据Buffer
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.size,
                GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer)
    }
}

package com.benhero.glstudio.renderer

import android.content.Context

import com.benhero.glstudio.util.ProjectionMatrixHelper

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 正交投影
 *
 * @author Benhero
 */
class L3_2_OrthoRenderer(context: Context) : L2_2_ShapeRenderer(context) {
    companion object {
        private val VERTEX_SHADER = "" +
                // mat4：4×4的矩阵
                "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                "void main()\n" +
                "{\n" +
                // 矩阵与向量相乘得到最终的位置
                "    gl_Position = u_Matrix * a_Position;\n" +
                "    gl_PointSize = 0.0;\n" +
                "}"
    }

    private var mProjectionMatrixHelper: ProjectionMatrixHelper? = null

    override val vertexShader: String
        get() = VERTEX_SHADER

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        super.onSurfaceCreated(glUnused, config)
        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(glUnused, width, height)
        mProjectionMatrixHelper!!.enable(width, height)
    }
}

package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 正交投影
 *
 * @author Benhero
 */
class L3_1_OrthoRenderer(context: Context) : L2_2_ShapeRenderer(context) {
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

    private var uMatrixLocation: Int = 0
    /**
     * 矩阵数组
     */
    private val mProjectionMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)

    override val vertexShader: String
        get() = VERTEX_SHADER

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        super.onSurfaceCreated(glUnused, config)
        uMatrixLocation = getUniform("u_Matrix")
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(glUnused, width, height)

        // 边长比(>=1)，非宽高比
        val aspectRatio = if (width > height)
            width.toFloat() / height.toFloat()
        else
            height.toFloat() / width.toFloat()

        // 1. 矩阵数组
        // 2. 结果矩阵起始的偏移量
        // 3. left：x的最小值
        // 4. right：x的最大值
        // 5. bottom：y的最小值
        // 6. top：y的最大值
        // 7. near：z的最小值
        // 8. far：z的最大值
        if (width > height) {
            // 横屏
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            // 竖屏or正方形
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
        // 更新u_Matrix的值，即更新矩阵数组
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0)
    }
}

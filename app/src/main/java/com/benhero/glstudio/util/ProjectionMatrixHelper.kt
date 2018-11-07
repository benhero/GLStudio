package com.benhero.glstudio.util

import android.opengl.GLES20
import android.opengl.Matrix

/**
 * 正交投影矩阵助手类
 *
 * @author Benhero
 */
class ProjectionMatrixHelper(program: Int, name: String) {

    private val uMatrixLocation: Int = GLES20.glGetUniformLocation(program, name)

    private val mProjectionMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)

    fun enable(width: Int, height: Int) {
        val aspectRatio = if (width > height)
            width.toFloat() / height.toFloat()
        else
            height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0)
    }

}

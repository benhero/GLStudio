package com.benhero.glstudio.base

import android.opengl.Matrix

/**
 * GL旋转动画
 *
 * @author Benhero
 */
class GLRotateAnimation : GLAnimation() {
    var fromDegrees: Float = 0.toFloat()
    var toDegrees: Float = 0.toFloat()

    override fun applyTransformation(percent: Float, `object`: GLObject) {
        super.applyTransformation(percent, `object`)
        val currentDegree = (toDegrees - fromDegrees) * percent + fromDegrees
        // (0,0,0) 与 (0,0,-1)作为旋转轴
        Matrix.rotateM(`object`.positionMatrix, 0, currentDegree, 0f, 0f, -1f)
    }
}

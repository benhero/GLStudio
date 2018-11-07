package com.benhero.glstudio.base

import android.opengl.Matrix

/**
 * GL缩放动画
 *
 * @author chenbenbin
 */
class GLScaleAnimation : GLAnimation() {
    var fromX = 1.0f
    var toX = 1.0f
    var fromY = 1.0f
    var toY = 1.0f
    private val mPivotXType: Int = 0

    override fun applyTransformation(percent: Float, `object`: GLObject) {
        super.applyTransformation(percent, `object`)
        val currentX = (toX - fromX) * percent + fromX
        val currentY = (toY - fromY) * percent + fromY
        Matrix.scaleM(`object`.positionMatrix, 0,
                currentX, currentY, 1f)
    }
}

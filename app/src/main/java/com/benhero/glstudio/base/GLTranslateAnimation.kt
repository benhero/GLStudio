package com.benhero.glstudio.base

import android.opengl.Matrix

/**
 * GL平移动画
 *
 * @author chenbenbin
 */
class GLTranslateAnimation : GLAnimation() {
    var fromX: Float = 0.toFloat()
    var fromY: Float = 0.toFloat()
    var toX: Float = 0.toFloat()
    var toY: Float = 0.toFloat()

    override fun applyTransformation(percent: Float, `object`: GLObject) {
        super.applyTransformation(percent, `object`)
        val currentX = (toX - fromX) * percent + fromX
        val currentY = (toY - fromY) * percent + fromY
        Matrix.translateM(`object`.positionMatrix, 0,
                `object`.xPositionToGL(currentX), `object`.yPositionToGL(currentY), 0f)
    }
}

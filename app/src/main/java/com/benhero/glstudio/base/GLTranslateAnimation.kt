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

    override fun applyTransformation(percent: Float, glObject: GLObject) {
        super.applyTransformation(percent, glObject)
        val currentX = (toX - fromX) * percent + fromX
        val currentY = (toY - fromY) * percent + fromY
        Matrix.translateM(glObject.positionMatrix, 0,
                glObject.xPositionToGL(currentX), glObject.yPositionToGL(currentY), 0f)
    }
}

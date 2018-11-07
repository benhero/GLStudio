package com.benhero.glstudio.base

import android.graphics.Color

/**
 * OpenGL绘制矩形对象
 *
 * @author Benhero
 */
class GLRectangle : GLObject() {
    var red: Float = 0.toFloat()
    var green: Float = 0.toFloat()
    var blue: Float = 0.toFloat()
    /**
     * 4个顶点的坐标
     */
    var position = floatArrayOf(-0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f)

    fun setColor(color: Int) {
        red = Color.red(color) / 255.0f
        green = Color.green(color) / 255.0f
        blue = Color.blue(color) / 255.0f
        mAlpha = Color.alpha(color) / 255.0f
    }
}

package com.benhero.glstudio.base

/**
 * OpenGL绘制对象
 *
 * @author Benhero
 */
open class GLObject {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var xgl: Float = 0.toFloat()
    var ygl: Float = 0.toFloat()
    var width: Float = 0.toFloat()
    var height: Float = 0.toFloat()
    var widthGL: Float = 0.toFloat()
    var heightGL: Float = 0.toFloat()
    var degree: Int = 0
    internal var mAlpha: Float = 0.toFloat()
    var alphas = floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f)
    var parentWidth: Int = 0
        internal set
    var parentHeight: Int = 0
        internal set
    var aspectRatioX: Float = 0.toFloat()
        internal set
    var aspectRatioY: Float = 0.toFloat()
        internal set
    var startTime: Long = 0
    var endTime: Long = 0

    var glAnimation: GLAnimation? = null

    var gravity = GRAVITY_NONE.toFloat()

    /**
     * 坐标矩阵
     */
    val positionMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)

    var alpha: Float
        get() = mAlpha
        set(alpha) {
            if (alpha < 0 || alpha > 1) {
                throw IllegalArgumentException("Alpha must between 0 to 1. Current value is $alpha")
            }
            mAlpha = alpha
            for (i in alphas.indices) {
                alphas[i] = alpha
            }
        }

    fun initialize(parentWidth: Int, parentHeight: Int, aspectRatioX: Float, aspectRatioY: Float) {
        this.parentWidth = parentWidth
        this.parentHeight = parentHeight
        this.aspectRatioX = aspectRatioX
        this.aspectRatioY = aspectRatioY
        if (gravity == GRAVITY_CENTER.toFloat()) {
            GLObjectUtils.setToCenter(this)
        } else if (gravity == GRAVITY_CENTER_HORIZONTAL.toFloat()) {
            GLObjectUtils.setToCenterX(this)
        } else if (gravity == GRAVITY_CENTER_VERTICAL.toFloat()) {
            GLObjectUtils.setToCenterY(this)
        }
    }

    /**
     * x坐标转换到GL坐标系上的位置
     */
    fun xPositionToGL(x: Float): Float {
        return (x - this.x) / (parentWidth / 2.0f) * aspectRatioX
    }

    /**
     * y坐标转换到GL坐标系上的位置
     */
    fun yPositionToGL(y: Float): Float {
        return (this.y - y * 2.0f) / parentHeight * aspectRatioY
    }

    /**
     * x方向位移转换到GL坐标系上的位置
     */
    fun xTranslateToGL(x: Float): Float {
        return (x - parentWidth / 2) / (parentWidth / 2) * aspectRatioX
    }

    /**
     * 重置矩阵：每帧操作之前都需要进行重置
     */
    fun resetMatrix() {
        System.arraycopy(DEFAULT_MATRIX, 0, positionMatrix, 0, positionMatrix.size)
    }

    companion object {
        val GRAVITY_NONE = 0
        val GRAVITY_CENTER = 1
        val GRAVITY_CENTER_VERTICAL = 2
        val GRAVITY_CENTER_HORIZONTAL = 3

        /**
         * 默认初始矩阵
         */
        private val DEFAULT_MATRIX = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)
    }
}

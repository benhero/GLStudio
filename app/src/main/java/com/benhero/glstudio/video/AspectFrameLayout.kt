package com.benhero.glstudio.video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout


/**
 * 自动适配比例的布局
 */
class AspectFrameLayout : FrameLayout {

    private var mTargetAspect = -1.0
    /**
     * 是否自动适配尺寸
     */
    private var mIsAutoFit = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setAspectRatio(aspectRatio: Double) {
        if (aspectRatio < 0) {
            throw IllegalArgumentException()
        }
        Log.w(TAG, "Setting aspect ratio to $aspectRatio (was $mTargetAspect)")
        if (mTargetAspect != aspectRatio) {
            mTargetAspect = aspectRatio
            requestLayout()
        }
    }

    fun setAutoFit(autoFit: Boolean) {
        mIsAutoFit = autoFit
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasure = widthMeasureSpec
        var heightMeasure = heightMeasureSpec
        if (!mIsAutoFit) {
            super.onMeasure(widthMeasure, heightMeasure)
            return
        }

        if (mTargetAspect > 0) {
            var initialWidth = View.MeasureSpec.getSize(widthMeasure)
            var initialHeight = View.MeasureSpec.getSize(heightMeasure)

            val horizontalPadding = paddingLeft + paddingRight
            val verticalPadding = paddingTop + paddingBottom
            initialWidth -= horizontalPadding
            initialHeight -= verticalPadding

            val viewAspectRatio = initialWidth.toDouble() / initialHeight
            val aspectDiff = mTargetAspect / viewAspectRatio - 1

            if (Math.abs(aspectDiff) < 0.01) {
                Log.w(TAG, "aspect ratio is good (target=" + mTargetAspect +
                        ", view=" + initialWidth + "x" + initialHeight + ")")
            } else {
                if (aspectDiff > 0) {
                    initialHeight = (initialWidth / mTargetAspect).toInt()
                } else {
                    initialWidth = (initialHeight * mTargetAspect).toInt()
                }
                initialWidth += horizontalPadding
                initialHeight += verticalPadding
                widthMeasure = View.MeasureSpec.makeMeasureSpec(initialWidth, View.MeasureSpec.EXACTLY)
                heightMeasure = View.MeasureSpec.makeMeasureSpec(initialHeight, View.MeasureSpec.EXACTLY)
            }
        }

        super.onMeasure(widthMeasure, heightMeasure)
    }

    companion object {
        private const val TAG = "AspectFrameLayout"
    }
}

package com.benhero.glstudio.base

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

/**
 * GL动画基类
 *
 * @author chenbenbin
 */
open class GLAnimation {
    var mStartTime: Long = 0
    var endTime: Long = 0
        protected set
    protected var mDuration: Long = 0

    var isStarted: Boolean = false
        protected set
    var isEnd: Boolean = false
        protected set

    /**
     * 动画结束后是否应用最后的效果
     */
    open var isFillAfter = false

    var interpolator: Interpolator = LinearInterpolator()

    protected var mListener: GLAnimationListener? = null

    open var startTime: Long
        get() = mStartTime
        set(startTime) {
            mStartTime = startTime
            updateEndTime()
        }

    open var duration: Long
        get() = mDuration
        set(duration) {
            mDuration = duration
            updateEndTime()
        }

    protected fun updateEndTime() {
        endTime = mStartTime + mDuration
    }

    fun isInAnimationTime(time: Long): Boolean {
        return time >= mStartTime && time <= endTime
    }

    fun setListener(listener: GLAnimationListener) {
        mListener = listener
    }

    /**
     * 获取当前的变化
     *
     * @param currentTime 执行时间
     * @param object      动画对象
     * @return 是否动画正在执行中(未开始也算执行中)
     */
    open fun getTransformation(currentTime: Long, `object`: GLObject): Boolean {
        if (currentTime > endTime) {
            if (!isEnd) {
                // 动画刚刚结束
                isEnd = true
                onAnimationEnd()
            }
            if (isFillAfter) {
                // 动画结束后，任然使用动画最后的效果
                applyTransformation(1f, `object`)
                onAnimationProgress(1f)
            }
            return false
        }
        if (currentTime >= mStartTime) {
            // 动画开始了
            if (!isStarted) {
                // 动画刚刚开始
                isStarted = true
                onAnimationStart()
            }
            val runTime = currentTime - mStartTime
            val percent = 1.0f * runTime / mDuration
            applyTransformation(interpolator.getInterpolation(percent), `object`)
            onAnimationProgress(percent)
        }
        return true
    }


    /**
     * 应用动画变化
     *
     * @param percent 动画进度百分比
     * @param object  动画对象
     */
    protected open fun applyTransformation(percent: Float, `object`: GLObject) {

    }

    protected fun onAnimationStart() {
        if (mListener != null) {
            mListener!!.onStart()
        }
    }

    protected fun onAnimationRepeat() {
        if (mListener != null) {

        }
    }

    protected fun onAnimationEnd() {
        if (mListener != null) {
            mListener!!.onEnd()
        }
    }

    protected fun onAnimationProgress(percent: Float) {
        if (mListener != null) {
            mListener!!.onProgress(percent)
        }
    }
}

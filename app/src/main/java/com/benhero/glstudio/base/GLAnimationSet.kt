package com.benhero.glstudio.base

import java.util.ArrayList

/**
 * GL动画集合
 *
 * @author Benhero
 */
class GLAnimationSet : GLAnimation() {
    private val mAnimations = ArrayList<GLAnimation>()

    val animations: List<GLAnimation>
        get() = mAnimations

    override var startTime: Long
        get() = super.startTime
        set(startTime) {
            super.startTime = startTime
            for (animation in mAnimations) {
                animation.startTime = startTime
            }
        }

    override var duration: Long
        get() = super.duration
        set(duration) {
            super.duration = duration
            for (animation in mAnimations) {
                animation.duration = duration
            }
        }

    override var isFillAfter: Boolean
        get() = super.isFillAfter
        set(fillAfter) {
            super.isFillAfter = fillAfter
            for (animation in mAnimations) {
                animation.isFillAfter = fillAfter
            }
        }

    fun addAnimation(animation: GLAnimation) {
        mAnimations.add(animation)
    }

    /**
     * 根据子动画更新动画集合的时间
     */
    fun updateTimeByChildren() {
        var startTime: Long = 0
        var endTime: Long = 0
        for (animation in mAnimations) {
            startTime = Math.min(mStartTime, animation.mStartTime)
            endTime = Math.max(animation.startTime + animation.duration, endTime)
        }
        mStartTime = startTime
        mDuration = endTime - startTime
    }

    override fun getTransformation(currentTime: Long, `object`: GLObject): Boolean {
        val result = super.getTransformation(currentTime, `object`)
        for (animation in mAnimations) {
            animation.getTransformation(currentTime, `object`)
        }
        return result
    }
}

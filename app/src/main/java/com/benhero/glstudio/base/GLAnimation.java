package com.benhero.glstudio.base;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * GL动画基类
 *
 * @author chenbenbin
 */
public class GLAnimation {
    protected long mStartTime;
    protected long mEndTime;
    protected long mDuration;

    protected boolean mIsStarted;
    protected boolean mIsEnd;
    private Interpolator mInterpolator = new LinearInterpolator();

    protected GLAnimationListener mListener;

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
        updateEndTime();
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
        updateEndTime();
    }

    protected void updateEndTime() {
        mEndTime = mStartTime + mDuration;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public boolean isEnd() {
        return mIsEnd;
    }

    public boolean isInAnimationTime(long time) {
        return time >= mStartTime && time <= mEndTime;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setListener(GLAnimationListener listener) {
        mListener = listener;
    }

    /**
     * 获取当前的变化
     *
     * @param currentTime 执行时间
     * @param object      动画对象
     * @return 是否动画正在执行中(未开始也算执行中)
     */
    public boolean getTransformation(long currentTime, GLObject object) {
        if (currentTime > mEndTime) {
            if (!mIsEnd) {
                // 动画刚刚结束
                mIsEnd = true;
                onAnimationEnd();
            }
            return false;
        }
        if (currentTime >= mStartTime) {
            // 动画开始了
            if (!mIsStarted) {
                // 动画刚刚开始
                mIsStarted = true;
                onAnimationStart();
            }
            long runTime = currentTime - mStartTime;
            float percent = 1.0f * runTime / mDuration;
            applyTransformation(mInterpolator.getInterpolation(percent), object);
            onAnimationProgress(percent);
        }
        return true;
    }


    /**
     * 应用动画变化
     *
     * @param percent 动画进度百分比
     * @param object  动画对象
     */
    protected void applyTransformation(float percent, GLObject object) {

    }

    protected void onAnimationStart() {
        if (mListener != null) {
            mListener.onStart();
        }
    }

    protected void onAnimationRepeat() {
        if (mListener != null) {

        }
    }

    protected void onAnimationEnd() {
        if (mListener != null) {
            mListener.onEnd();
        }
    }

    protected void onAnimationProgress(float percent) {
        if (mListener != null) {
            mListener.onProgress(percent);
        }
    }
}

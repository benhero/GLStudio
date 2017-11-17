package com.benhero.glstudio.base;

import android.view.animation.Interpolator;

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
    private Interpolator mInterpolator;

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
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
}

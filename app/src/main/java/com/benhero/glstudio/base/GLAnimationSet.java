package com.benhero.glstudio.base;

import java.util.ArrayList;
import java.util.List;

/**
 * GL动画集合
 *
 * @author Benhero
 */
public class GLAnimationSet extends GLAnimation {
    private List<GLAnimation> mAnimations = new ArrayList<>();

    public void addAnimation(GLAnimation animation) {
        mAnimations.add(animation);
    }

    public List<GLAnimation> getAnimations() {
        return mAnimations;
    }

    @Override
    public void setStartTime(long startTime) {
        super.setStartTime(startTime);
        for (GLAnimation animation : mAnimations) {
            animation.setStartTime(startTime);
        }
    }

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
        for (GLAnimation animation : mAnimations) {
            animation.setDuration(duration);
        }
    }

    /**
     * 根据子动画更新动画集合的时间
     */
    public void updateTimeByChildren() {
        long startTime = 0;
        long endTime = 0;
        for (GLAnimation animation : mAnimations) {
            startTime = Math.min(mStartTime, animation.mStartTime);
            endTime = Math.max(animation.getStartTime() + animation.getDuration(), endTime);
        }
        mStartTime = startTime;
        mDuration = endTime - startTime;
    }

    @Override
    public boolean getTransformation(long currentTime, GLObject object) {
        boolean result = super.getTransformation(currentTime, object);
        for (GLAnimation animation : mAnimations) {
            animation.getTransformation(currentTime, object);
        }
        return result;
    }

    @Override
    public void setFillAfter(boolean fillAfter) {
        super.setFillAfter(fillAfter);
        for (GLAnimation animation : mAnimations) {
            animation.setFillAfter(fillAfter);
        }
    }
}

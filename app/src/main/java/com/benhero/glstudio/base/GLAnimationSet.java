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

    @Override
    public boolean getTransformation(long currentTime, GLObject object) {
        boolean result = super.getTransformation(currentTime, object);
        for (GLAnimation animation : mAnimations) {
            animation.getTransformation(currentTime, object);
        }
        return result;
    }
}

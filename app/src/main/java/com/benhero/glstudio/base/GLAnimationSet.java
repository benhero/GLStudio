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
}

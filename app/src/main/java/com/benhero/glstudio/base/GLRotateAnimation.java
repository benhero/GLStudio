package com.benhero.glstudio.base;

/**
 * GL旋转动画
 *
 * @author Benhero
 */
public class GLRotateAnimation extends GLAnimation {
    private float mFromDegrees;
    private float mToDegrees;

    public float getFromDegrees() {
        return mFromDegrees;
    }

    public void setFromDegrees(float fromDegrees) {
        mFromDegrees = fromDegrees;
    }

    public float getToDegrees() {
        return mToDegrees;
    }

    public void setToDegrees(float toDegrees) {
        mToDegrees = toDegrees;
    }
}

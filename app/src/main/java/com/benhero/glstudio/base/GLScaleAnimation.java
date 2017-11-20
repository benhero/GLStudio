package com.benhero.glstudio.base;

/**
 * GL缩放动画
 *
 * @author chenbenbin
 */
public class GLScaleAnimation extends GLAnimation {
    private float mFromX = 1.0f;
    private float mToX = 1.0f;
    private float mFromY = 1.0f;
    private float mToY = 1.0f;
    private int mPivotXType;

    public float getFromX() {
        return mFromX;
    }

    public void setFromX(float fromX) {
        mFromX = fromX;
    }

    public float getToX() {
        return mToX;
    }

    public void setToX(float toX) {
        mToX = toX;
    }

    public float getFromY() {
        return mFromY;
    }

    public void setFromY(float fromY) {
        mFromY = fromY;
    }

    public float getToY() {
        return mToY;
    }

    public void setToY(float toY) {
        mToY = toY;
    }
}

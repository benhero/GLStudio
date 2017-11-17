package com.benhero.glstudio.base;

/**
 * GL平移动画
 *
 * @author chenbenbin
 */
public class GLTranslateAnimation extends GLAnimation {
    private float mFromX;
    private float mFromY;
    private float mToX;
    private float mToY;

    public float getFromX() {
        return mFromX;
    }

    public void setFromX(float fromX) {
        mFromX = fromX;
    }

    public float getFromY() {
        return mFromY;
    }

    public void setFromY(float fromY) {
        mFromY = fromY;
    }

    public float getToX() {
        return mToX;
    }

    public void setToX(float toX) {
        mToX = toX;
    }

    public float getToY() {
        return mToY;
    }

    public void setToY(float toY) {
        mToY = toY;
    }
}

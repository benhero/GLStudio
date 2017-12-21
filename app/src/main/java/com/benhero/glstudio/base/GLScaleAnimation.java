package com.benhero.glstudio.base;

import android.opengl.Matrix;

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

    @Override
    protected void applyTransformation(float percent, GLObject object) {
        super.applyTransformation(percent, object);
        float currentX = (mToX - mFromX) * percent + mFromX;
        float currentY = (mToY - mFromY) * percent + mFromY;
        Matrix.scaleM(object.getPositionMatrix(), 0,
                currentX, currentY, 1);
    }
}

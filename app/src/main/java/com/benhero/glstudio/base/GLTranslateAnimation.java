package com.benhero.glstudio.base;

import android.opengl.Matrix;

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

    @Override
    protected void applyTransformation(float percent, GLObject object) {
        super.applyTransformation(percent, object);
        float currentX = (mToX - mFromX) * percent + mFromX;
        float currentY = (mToY - mFromY) * percent + mFromY;
        Matrix.translateM(object.getPositionMatrix(), 0,
                object.xPositionToGL(currentX), object.yPositionToGL(currentY), 0);
    }
}

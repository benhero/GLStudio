package com.benhero.glstudio.base;

import android.graphics.Color;

/**
 * OpenGL绘制矩形对象
 *
 * @author Benhero
 */
public class GLRectangle extends GLObject {
    private float mRed;
    private float mGreen;
    private float mBlue;
    /**
     * 4个顶点的坐标
     */
    private float[] mPosition = new float[]{
            -0.5f, 0.5f,
            0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f,
    };

    public float[] getPosition() {
        return mPosition;
    }

    public void setPosition(float[] position) {
        this.mPosition = position;
    }

    public float getRed() {
        return mRed;
    }

    public void setRed(float red) {
        mRed = red;
    }

    public float getGreen() {
        return mGreen;
    }

    public void setGreen(float green) {
        mGreen = green;
    }

    public float getBlue() {
        return mBlue;
    }

    public void setBlue(float blue) {
        mBlue = blue;
    }

    public void setColor(int color) {
        mRed = Color.red(color) / 255.0f;
        mGreen = Color.green(color) / 255.0f;
        mBlue = Color.blue(color) / 255.0f;
        mAlpha = Color.alpha(color) / 255.0f;
    }
}

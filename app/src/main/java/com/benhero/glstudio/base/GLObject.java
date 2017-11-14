package com.benhero.glstudio.base;

/**
 * OpenGL绘制对象
 *
 * @author chenbenbin
 */
public class GLObject {
    float mX;
    float mY;
    float mXGL;
    float mYGL;
    float mWidth;
    float mHeight;
    float mWidthGL;
    float mHeightGL;
    int mDegree;
    float mAlpha;

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getXGL() {
        return mXGL;
    }

    public void setXGL(float XGL) {
        mXGL = XGL;
    }

    public float getYGL() {
        return mYGL;
    }

    public void setYGL(float YGL) {
        mYGL = YGL;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        mWidth = width;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        mHeight = height;
    }

    public float getWidthGL() {
        return mWidthGL;
    }

    public void setWidthGL(float widthGL) {
        mWidthGL = widthGL;
    }

    public float getHeightGL() {
        return mHeightGL;
    }

    public void setHeightGL(float heightGL) {
        mHeightGL = heightGL;
    }

    public int getDegree() {
        return mDegree;
    }

    public void setDegree(int degree) {
        mDegree = degree;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }
}

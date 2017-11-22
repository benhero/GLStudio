package com.benhero.glstudio.base;

/**
 * OpenGL绘制对象
 *
 * @author Benhero
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
    float[] mAlphas = new float[16];
    int mParentWidth;
    int mParentHeight;
    float mAspectRatioX;
    float mAspectRatioY;

    GLAnimation mGLAnimation;

    /**
     * 坐标矩阵
     */
    private float[] mPositionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    /**
     * 默认初始矩阵
     */
    private static final float[] DEFAULT_MATRIX = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

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
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha must between 0 to 1. Current value is " + alpha);
        }
        mAlpha = alpha;
        for (int i = 0; i < mAlphas.length; i++) {
            mAlphas[i] = alpha;
        }
    }

    public float[] getAlphas() {
        return mAlphas;
    }

    public void setAlphas(float[] alphas) {
        mAlphas = alphas;
    }

    public int getParentWidth() {
        return mParentWidth;
    }

    public int getParentHeight() {
        return mParentHeight;
    }

    public float getAspectRatioX() {
        return mAspectRatioX;
    }

    public float getAspectRatioY() {
        return mAspectRatioY;
    }

    public void initialize(int parentWidth, int parentHeight, float aspectRatioX, float aspectRatioY) {
        mParentWidth = parentWidth;
        mParentHeight = parentHeight;
        mAspectRatioX = aspectRatioX;
        mAspectRatioY = aspectRatioY;
    }

    /**
     * x坐标转换到GL坐标系上的位置
     */
    public float xPositionToGL(float x) {
        return (x - mParentWidth / 2) / (mParentWidth / 2) * mAspectRatioX;
    }

    /**
     * y坐标转换到GL坐标系上的位置
     */
    public float yPositionToGL(float y) {
        return (mParentHeight - y * 2) / mParentHeight * mAspectRatioY;
    }

    public GLAnimation getGLAnimation() {
        return mGLAnimation;
    }

    public void setGLAnimation(GLAnimation GLAnimation) {
        mGLAnimation = GLAnimation;
    }

    /**
     * 重置矩阵：每帧操作之前都需要进行重置
     */
    public void resetMatrix() {
        System.arraycopy(DEFAULT_MATRIX, 0, mPositionMatrix, 0, mPositionMatrix.length);
    }

    public float[] getPositionMatrix() {
        return mPositionMatrix;
    }
}

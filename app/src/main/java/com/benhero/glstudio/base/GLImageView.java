package com.benhero.glstudio.base;

/**
 * OpenGL绘制图片对象
 *
 * @author Benhero
 */
public class GLImageView extends GLObject {
    private int mTextureId;
    private int mResId;

    private float[] mPosition = new float[8];

    public float[] getPosition() {
        return mPosition;
    }

    public void setPosition(float[] position) {
        this.mPosition = position;
    }

    public int getTextureId() {
        return mTextureId;
    }

    public void setTextureId(int textureId) {
        mTextureId = textureId;
    }

    public void updatePosition() {
        mPosition[0] = mXGL;
        mPosition[1] = mYGL;
        mPosition[2] = mXGL + mWidthGL;
        mPosition[3] = mYGL;
        mPosition[4] = mXGL + mWidthGL;
        mPosition[5] = mYGL - mHeightGL;
        mPosition[6] = mXGL;
        mPosition[7] = mYGL - mHeightGL;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }
}

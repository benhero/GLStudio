package com.benhero.glstudio.base;

import android.graphics.Bitmap;

/**
 * OpenGL绘制图片对象
 *
 * @author Benhero
 */
public class GLImageView extends GLObject {
    private int mTextureId;
    private int mResId;
    private Bitmap mBitmap;

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

    public int getTextureId() {
        return mTextureId;
    }

    public void setTextureId(int textureId) {
        mTextureId = textureId;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}

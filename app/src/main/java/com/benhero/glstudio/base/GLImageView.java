package com.benhero.glstudio.base;

/**
 * OpenGL绘制图片对象
 *
 * @author Benhero
 */
public class GLImageView extends GLObject {
    private int mTextureId;
    private int mResId;

    /**
     * 4个顶点的坐标
     */
    private float[] mPosition = new float[]{
            -0.5f, 0.5f,
            0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f,
    };

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

    /**
     * 重置矩阵：每帧操作之前都需要进行重置
     */
    public void resetMatrix() {
        System.arraycopy(DEFAULT_MATRIX, 0, mPositionMatrix, 0, mPositionMatrix.length);
    }

    public float[] getPositionMatrix() {
        return mPositionMatrix;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }
}

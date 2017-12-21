package com.benhero.glstudio.base;

import android.opengl.Matrix;

/**
 * GL旋转动画
 *
 * @author Benhero
 */
public class GLRotateAnimation extends GLAnimation {
    private float mFromDegrees;
    private float mToDegrees;

    public float getFromDegrees() {
        return mFromDegrees;
    }

    public void setFromDegrees(float fromDegrees) {
        mFromDegrees = fromDegrees;
    }

    public float getToDegrees() {
        return mToDegrees;
    }

    public void setToDegrees(float toDegrees) {
        mToDegrees = toDegrees;
    }

    @Override
    protected void applyTransformation(float percent, GLObject object) {
        super.applyTransformation(percent, object);
        float currentDegree = (mToDegrees - mFromDegrees) * percent + mFromDegrees;
        // (0,0,0) 与 (0,0,-1)作为旋转轴
        Matrix.rotateM(object.getPositionMatrix(), 0, currentDegree, 0, 0, -1);
    }
}

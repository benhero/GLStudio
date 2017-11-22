package com.benhero.glstudio.base;

/**
 * GL透明度动画
 *
 * @author chenbenbin
 */
public class GLAlphaAnimation extends GLAnimation {
    private float mFromAlpha;
    private float mToAlpha;

    public float getFromAlpha() {
        return mFromAlpha;
    }

    public void setFromAlpha(float fromAlpha) {
        mFromAlpha = fromAlpha;
    }

    public float getToAlpha() {
        return mToAlpha;
    }

    public void setToAlpha(float toAlpha) {
        mToAlpha = toAlpha;
    }

    @Override
    protected void applyTransformation(float percent, GLObject object) {
        super.applyTransformation(percent, object);
        object.setAlpha(percent);
    }
}

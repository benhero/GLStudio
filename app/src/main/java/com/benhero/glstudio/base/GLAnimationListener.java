package com.benhero.glstudio.base;

/**
 * GL动画回调接口
 *
 * @author Benhero
 */
public interface GLAnimationListener {
    void onStart();

    void onEnd();

    void onProgress(float percent);
}

package com.benhero.glstudio.base

/**
 * GL动画回调接口
 *
 * @author Benhero
 */
interface GLAnimationListener {
    fun onStart()

    fun onEnd()

    fun onProgress(percent: Float)
}

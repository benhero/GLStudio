package com.benhero.glstudio.base

import android.content.Context
import android.opengl.GLSurfaceView
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * GL动画渲染基础类
 *
 * @author Benhero
 */
open class AnimationRenderer(context: Context) : GLSurfaceView.Renderer {
    protected var mContext: Context
    protected var mGLImageViews: MutableList<GLImageView> = ArrayList()

    protected var mGLAnimations: List<GLAnimation> = ArrayList()

    init {
        mContext = context.applicationContext
    }

    fun addGLImageView(view: GLImageView) {
        mGLImageViews.add(view)
    }

    fun setGLImageViews(views: MutableList<GLImageView>) {
        mGLImageViews = views
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10) {

    }
}

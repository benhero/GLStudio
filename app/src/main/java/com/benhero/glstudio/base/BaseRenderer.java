package com.benhero.glstudio.base;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * GL渲染基础类
 *
 * @author Benhero
 */
public class BaseRenderer implements GLSurfaceView.Renderer {
    protected Context mContext;
    protected List<GLImageView> mGLImageViews = new ArrayList<>();

    protected List<GLAnimation> mGLAnimations = new ArrayList<>();

    public BaseRenderer(Context context) {
        mContext = context.getApplicationContext();
    }

    public void addGLImageView(GLImageView view) {
        mGLImageViews.add(view);
    }

    public void setGLImageViews(List<GLImageView> views) {
        mGLImageViews = views;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}

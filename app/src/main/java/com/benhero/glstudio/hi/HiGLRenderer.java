package com.benhero.glstudio.hi;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.benhero.glstudio.GLUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * com.benhero.glstudio.hi
 *
 * @author benhero
 */
public class HiGLRenderer implements GLSurfaceView.Renderer {
    //顶点数组
    private float[] mArray = {
            -0.5f, -0.5f, 0f,
            -0.5f, 0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f};

    //顶点颜色数组
    private float[] mColorArray = {
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f
    };

    // 缓冲区
    private FloatBuffer mBuffer;
    private FloatBuffer mColorBuffer;

    public HiGLRenderer() {
        //获取浮点形缓冲数据
        mBuffer = GLUtils.getFloatBuffer(mArray);
        //获取浮点型颜色数据
        mColorBuffer = GLUtils.getFloatBuffer(mColorArray);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置清屏颜色,但不执行清屏操作
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
        // 设置深度
//        gl.glClearDepthf(0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置窗口大小
        gl.glViewport(0, 0, width, width);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //设置为单位矩阵
        gl.glLoadIdentity();

//        GLU.gluLookAt(gl, 0f, 0f, 0f, 0f, 0f, -1f, 1f, 0f, 0f);

        // 清除屏幕
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // 允许设置顶点 // GL10.GL_VERTEX_ARRAY顶点数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 开启颜色渲染功能.
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

//        //开启深度测试
//        gl.glEnable(GL10.GL_DEPTH_TEST);

//        //指定深度测试模式
//        gl.glDepthFunc(GL10.GL_GREATER);

        // 设置顶点
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBuffer);

        //设置点的颜色为绿色
//        gl.glColor4f(0f, 1f, 0f, 0f);

        // 设置三角形顶点的颜色
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);

        //设置点的大小
//        gl.glPointSize(80f);

        // 绘制点
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);


//        //关闭深度测试
//        gl.glDisable(GL10.GL_DEPTH_TEST);

        // 取消顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //关闭颜色渲染功能.
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}

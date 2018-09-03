package com.benhero.glstudio.renderer;

import android.content.Context;
import android.opengl.GLES20;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.util.BufferUtil;

import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 动态改变顶点位置 & 颜色
 *
 * @author Benhero
 */
public class P1_1_PointRenderer extends BaseRenderer {
    private static final String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = 100.0;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";
    private FloatBuffer mVertexData;
    private int uColorLocation;
    private static final float[] POINT_DATA = {
            0f, 0f,
    };
    private static final int POSITION_COMPONENT_COUNT = 2;

    private Random mRandom = new Random();

    public P1_1_PointRenderer(Context context) {
        super(context);
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        int aPositionLocation = getAttrib("a_Position");
        uColorLocation = getUniform("u_Color");

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 只要持有传递给GL层的Buffer引用，就可以动态改变相关的数据信息
        mVertexData.put(new float[]{
                0.9f * mRandom.nextFloat() * (mRandom.nextFloat() > 0.5f ? 1 : -1),
                0.9f * mRandom.nextFloat() * (mRandom.nextFloat() > 0.5f ? 1 : -1)});
        mVertexData.position(0);

        GLES20.glUniform4f(uColorLocation, mRandom.nextFloat(), mRandom.nextFloat(), mRandom.nextFloat(), 1.0f);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
}
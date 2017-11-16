/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.benhero.glstudio.l1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.benhero.glstudio.util.LoggerConfig;
import com.benhero.glstudio.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 图形
 *
 * @author Benhero
 */
public class ShapeRenderer2 implements Renderer {
    /**
     * 顶点着色器：之后定义的每个都会传1次给顶点着色器
     */
    private static final String VERTEX_SHADER = "" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = a_Position;\n" +
            "    gl_PointSize = 40.0;\n" +
            "}";
    /**
     * 片段着色器
     */
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "uniform vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private final Context mContext;
    private int mProgram;
    private final FloatBuffer mVertexData;
    private int uColorLocation;
    private int aPositionLocation;
    /**
     * 顶点数据数组
     */
    private static final float[] POINT_DATA = {
            // 两个点的x,y坐标（x，y各占1个分量）
            -0.5f, 0f,
            -0.5f, 0.5f,
            0f, 0.5f,
            0f, 0,
            0.5f, -0.25f,
            0f, -0.7f,
    };
    /**
     * 每个顶点数据关联的分量个数：当前案例只有x、y，故为2
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    /**
     * Float类型占4Byte
     */
    private static final int BYTES_PER_FLOAT = 4;

    public ShapeRenderer2(Context context) {
        mContext = context;

        // 分配一个块Native内存，用于与GL通讯传递。(我们通常用的数据存在于Dalvik的内存中，1.无法访问硬件；2.会被垃圾回收)
        mVertexData = ByteBuffer
                // 分配顶点坐标分量个数 * Float占的Byte位数
                .allocateDirect(POINT_DATA.length * BYTES_PER_FLOAT)
                // 按照本地字节序排序
                .order(ByteOrder.nativeOrder())
                // Byte类型转Float类型
                .asFloatBuffer();

        // 将Dalvik的内存数据复制到Native内存中
        mVertexData.put(POINT_DATA);
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        int vertexShader = ShaderHelper.compileVertexShader(VERTEX_SHADER);
        int fragmentShader = ShaderHelper.compileFragmentShader(FRAGMENT_SHADER);

        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }

        GLES20.glUseProgram(mProgram);

        uColorLocation = GLES20.glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData);

        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f);
        // 几何图形相关定义：http://wiki.jikexueyuan.com/project/opengl-es-guide/basic-geometry-definition.html
        drawTriangle();
        drawLine();
        drawPoint();
    }

    private void drawPoint() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }

    private void drawLine() {
        // GL_LINES：每2个点构成一条线段
        // GL_LINE_LOOP：按顺序将所有的点连接起来，包括首位相连
        // GL_LINE_STRIP：按顺序将所有的点连接起来，不包括首位相连
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }

    private void drawTriangle() {
        // GL_TRIANGLES：每3个点构成一个三角形
        // GL_TRIANGLE_FAN：第一个点和之后所有相邻的2个点构成一个三角形
        // GL_TRIANGLE_STRIP：相邻3个点构成一个三角形,不包括首位两个点
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }
}
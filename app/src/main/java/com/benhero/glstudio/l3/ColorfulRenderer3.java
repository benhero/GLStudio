package com.benhero.glstudio.l3;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.benhero.glstudio.util.LoggerConfig;
import com.benhero.glstudio.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 渐变色
 *
 * @author Benhero
 */
public class ColorfulRenderer3 implements Renderer {
    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            // vec4：4个分量的向量：r、g、b、a
            // a_Color：从外部传递进来的每个顶点的颜色值
            "attribute vec4 a_Color;\n" +
            // v_Color：将每个顶点的颜色值传递给片段着色器
            "varying vec4 v_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    v_Color = a_Color;\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            // v_Color：从顶点着色器传递过来的颜色值
            "varying vec4 v_Color;\n" +
            "void main()\t\t\n" +
            "{\n" +
            "    gl_FragColor = v_Color;\n" +
            "}";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_COLOR = "a_Color";
    private final Context mContext;
    private int mProgram;
    private final FloatBuffer mVertexData;
    private int aColorLocation;
    private int aPositionLocation;

    private static final float[] POINT_DATA = {
            // 一个顶点有5个向量数据：x、y、r、g、b
            -0.5f, -0.5f, 1f, 1f, 1f,
            0.5f, -0.5f, 1f, 0f, 1f,
            -0.5f, 0.5f, 0f, 1f, 1f,
            0.5f, 0.5f, 1f, 1f, 0f,
    };
    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    /**
     * 颜色占用的向量个数
     */
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;

    /**
     * 数据数组中每个顶点起始数据的间距：数组中每个顶点相关属性占的Byte值
     */
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private int uMatrixLocation;
    private final float[] projectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    public ColorfulRenderer3(Context context) {
        mContext = context;

        mVertexData = ByteBuffer
                .allocateDirect(POINT_DATA.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

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

        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation,
                POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 将数组的初始读取位置右移2位，所以数组读取的顺序是r1, g1, b1, x2, y2, r2, g2, b2...
        mVertexData.position(POSITION_COMPONENT_COUNT);
        // COLOR_COMPONENT_COUNT：从数组中每次读取3个向量
        // STRIDE：每次读取间隔是 (2个位置 + 3个颜色值) * Float占的Byte位
        GLES20.glVertexAttribPointer(aColorLocation,
                COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, mVertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawRectangle();
    }

    private void drawRectangle() {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}

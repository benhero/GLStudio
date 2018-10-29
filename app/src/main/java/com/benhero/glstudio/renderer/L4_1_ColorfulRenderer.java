package com.benhero.glstudio.renderer;

import android.content.Context;
import android.opengl.GLES20;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.util.BufferUtil;
import com.benhero.glstudio.util.ProjectionMatrixHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 渐变色
 *
 * @author Benhero
 */
public class L4_1_ColorfulRenderer extends BaseRenderer {
    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            // a_Color：从外部传递进来的每个顶点的颜色值
            "attribute vec4 a_Color;\n" +
            // v_Color：将每个顶点的颜色值传递给片段着色器
            "varying vec4 v_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    v_Color = a_Color;\n" +
            "    gl_PointSize = 30.0;\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            // v_Color：从顶点着色器传递过来的颜色值
            "varying vec4 v_Color;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = v_Color;\n" +
            "}";
    private final FloatBuffer mVertexData;
    private final FloatBuffer mColorData;
    private ProjectionMatrixHelper mProjectionMatrixHelper;

    private static final float[] POINT_DATA = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
    };
    private static final float[] COLOR_DATA = {
            // 一个顶点有3个向量数据：r、g、b
            1f, 0.5f, 0.5f,
            1f, 0f, 1f,
            0f, 1f, 1f,
            1f, 1f, 0f,
    };


    /**
     * 坐标占用的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;
    /**
     * 颜色占用的向量个数
     */
    private static final int COLOR_COMPONENT_COUNT = 3;

    public L4_1_ColorfulRenderer(Context context) {
        super(context);
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
        mColorData = BufferUtil.createFloatBuffer(COLOR_DATA);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        int aPositionLocation = getAttrib("a_Position");
        int aColorLocation = getAttrib("a_Color");
        mProjectionMatrixHelper = new ProjectionMatrixHelper(getProgram(), "u_Matrix");

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation,
                POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        mColorData.position(0);
        GLES20.glVertexAttribPointer(aColorLocation,
                COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mColorData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mProjectionMatrixHelper.enable(width, height);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);
    }

}

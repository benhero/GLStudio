package com.benhero.glstudio.renderer;

import android.content.Context;

import com.benhero.glstudio.util.ProjectionMatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 正交投影
 *
 * @author Benhero
 */
public class L3_2_OrthoRenderer extends L2_2_ShapeRenderer {
    private static final String VERTEX_SHADER = "" +
            // mat4：4×4的矩阵
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            // 矩阵与向量相乘得到最终的位置
            "    gl_Position = u_Matrix * a_Position;\n" +
            "    gl_PointSize = 0.0;\n" +
            "}";

    private ProjectionMatrixHelper mProjectionMatrixHelper;

    public L3_2_OrthoRenderer(Context context) {
        super(context);
    }

    public String getVertexShader() {
        return VERTEX_SHADER;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        super.onSurfaceCreated(glUnused, config);
        mProjectionMatrixHelper = new ProjectionMatrixHelper(getProgram(), "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);
        mProjectionMatrixHelper.enable(width, height);
    }
}

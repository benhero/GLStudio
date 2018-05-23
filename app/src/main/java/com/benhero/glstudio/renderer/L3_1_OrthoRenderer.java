package com.benhero.glstudio.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 正交投影
 *
 * @author Benhero
 */
public class L3_1_OrthoRenderer extends L2_2_ShapeRenderer {
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

    private int uMatrixLocation;
    /**
     * 矩阵数组
     */
    private final float[] projectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    public L3_1_OrthoRenderer(Context context) {
        super(context);
    }

    public String getVertexShader() {
        return VERTEX_SHADER;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        super.onSurfaceCreated(glUnused, config);
        uMatrixLocation = getUniform("u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);

        // 边长比(>=1)，非宽高比
        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        // 1. 矩阵数组
        // 2. 结果矩阵起始的偏移量
        // 3. left：x的最小值
        // 4. right：x的最大值
        // 5. bottom：y的最小值
        // 6. top：y的最大值
        // 7. near：z的最小值
        // 8. far：z的最大值
        if (width > height) {
            // 横屏
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // 竖屏or正方形
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        // 更新u_Matrix的值，即更新矩阵数组
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
    }
}

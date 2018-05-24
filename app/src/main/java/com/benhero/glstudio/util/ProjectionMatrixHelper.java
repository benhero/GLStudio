package com.benhero.glstudio.util;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 正交投影矩阵助手类
 *
 * @author Benhero
 */
public class ProjectionMatrixHelper {

    private int uMatrixLocation;

    private final float[] mProjectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    public ProjectionMatrixHelper(int program, String name) {
        uMatrixLocation = GLES20.glGetUniformLocation(program, name);
    }

    public void enable(int width, int height) {
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }

}

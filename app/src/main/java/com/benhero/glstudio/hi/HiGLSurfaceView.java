package com.benhero.glstudio.hi;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * com.benhero.glstudio.hi
 *
 * @author chenbenbin
 */
public class HiGLSurfaceView extends GLSurfaceView {

    public HiGLSurfaceView(Context context) {
        super(context);
        setRenderer(new HiGLRenderer());
    }

}

package com.benhero.glstudio.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.benhero.glstudio.util.BufferUtil;
import com.benhero.glstudio.util.LoggerConfig;
import com.benhero.glstudio.util.ShaderHelper;

import java.nio.ByteBuffer;

/**
 * GL渲染基础类
 *
 * @author Benhero
 */
public abstract class BaseRenderer implements GLSurfaceView.Renderer {

    protected int mProgram;
    protected Context mContext;
    protected RendererCallback mRendererCallback;
    /**
     * 是否读取当前画面帧
     */
    protected boolean mIsReadCurrentFrame = false;

    public BaseRenderer(Context context) {
        mContext = context;
    }

    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected void makeProgram(String vertexShader, String fragmentShader) {
        // 步骤1：编译顶点着色器
        int vertexShaderId = ShaderHelper.compileVertexShader(vertexShader);
        // 步骤2：编译片段着色器
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader);
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        mProgram = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }

        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(mProgram);
    }

    protected int getUniform(String name) {
        return GLES20.glGetUniformLocation(mProgram, name);
    }

    protected int getAttrib(String name) {
        return GLES20.glGetAttribLocation(mProgram, name);
    }

    /**
     * 渲染完毕的回调
     */
    public interface RendererCallback {
        /**
         * 渲染完毕
         *
         * @param data   缓存数据
         * @param width  数据宽度
         * @param height 数据高度
         */
        void onRendererDone(ByteBuffer data, int width, int height);
    }

    public void setRendererCallback(RendererCallback rendererCallback) {
        this.mRendererCallback = rendererCallback;
    }

    public void setReadCurrentFrame(boolean readCurrentFrame) {
        mIsReadCurrentFrame = readCurrentFrame;
    }

    /**
     * 获取当前画面帧,并回调接口
     */
    protected void readPixels(int x, int y, int width, int height) {
        if (!mIsReadCurrentFrame) {
            return;
        }
        mIsReadCurrentFrame = false;
        ByteBuffer buffer = ByteBuffer.allocate(width * height * BufferUtil.BYTES_PER_FLOAT);
        GLES20.glReadPixels(x,
                y,
                width,
                height,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, buffer);
        if (mRendererCallback != null) {
            mRendererCallback.onRendererDone(buffer, width, height);
        }
    }
}

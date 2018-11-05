package com.benhero.glstudio.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.benhero.glstudio.R;
import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.util.BufferUtil;
import com.benhero.glstudio.util.TextureHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * FrameBuffer的使用：屏幕外渲染
 *
 * @author Benhero
 */
public class L7_1_FBORenderer extends BaseRenderer {
    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            "attribute vec2 a_TexCoord;\n" +
            "varying vec2 v_TexCoord;\n" +
            "void main()\n" +
            "{\n" +
            "    v_TexCoord = a_TexCoord;\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_TexCoord;\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "void main()\n" +
            "{\n" +
            "    vec4 pic = texture2D(u_TextureUnit, v_TexCoord);\n" +
            "    float gray = (pic.r + pic.g + pic.b) / 3.0f;\n" +
            "    gl_FragColor = vec4(gray, gray, gray, pic.a); \n" +
            "}";

    private final FloatBuffer mVertexData;
    private int uMatrixLocation;
    private int uTextureUnitLocation;

    private static final int POSITION_COMPONENT_COUNT = 2;

    private static final float[] POINT_DATA = {
            -1f, -1f,
            -1f, 1f,
            1f, 1f,
            1f, -1f,
    };

    private final float[] mProjectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    /**
     * 纹理坐标
     */
    private static final float[] TEX_VERTEX = {
            0, 1,
            0, 0,
            1, 0,
            1, 1,
    };
    /**
     * 纹理坐标中每个点占的向量个数
     */
    private static final int TEX_VERTEX_COMPONENT_COUNT = 2;
    private final FloatBuffer mTexVertexBuffer;
    /**
     * 纹理数据
     */
    private TextureHelper.TextureBean mTextureBean;

    private int[] mFrameBuffer = new int[1];
    private int[] mTexture = new int[1];

    public L7_1_FBORenderer(Context context) {
        super(context);
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA);
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        int aPositionLocation = getAttrib("a_Position");
        uMatrixLocation = getUniform("u_Matrix");

        int aTexCoordLocation = getAttrib("a_TexCoord");
        uTextureUnitLocation = getUniform("u_TextureUnit");

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        // 纹理数据
        mTextureBean = TextureHelper.loadTexture(getContext(), R.drawable.pikachu);

        // 由于Android屏幕上绘制的起始点在左上角，而GL纹理坐标是在左下角，所以需要进行水平翻转，即Y轴翻转
        Matrix.scaleM(mProjectionMatrix, 0, 1, -1, 1);

        GLES20.glClearColor(0f, 0f, 0f, 0f);
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        if (!isReadCurrentFrame()) {
            return;
        }
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // 1. 创建FrameBuffer、纹理对象
        createEnv();
        // 2. 配置FrameBuffer相关的绘制存储信息，并且绑定到当前的绘制环境上
        bindFrameBufferInfo();
        // 3. 更新视图区域
        GLES20.glViewport(0, 0, mTextureBean.getWidth(), mTextureBean.getHeight());
        // 4. 绘制图片
        drawTexture();
        // 5. 读取当前画面上的像素信息
        onReadPixel(0, 0, mTextureBean.getWidth(), mTextureBean.getHeight());
        // 6. 解绑FrameBuffer
        unbindFrameBufferInfo();
        // 7. 删除FrameBuffer、纹理对象
        deleteEnv();
    }

    private void createEnv() {
        // 1. 创建FrameBuffer
        GLES20.glGenFramebuffers(1, mFrameBuffer, 0);

        // 2.1 生成纹理对象
        GLES20.glGenTextures(1, mTexture, 0);
        // 2.2 绑定纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture[0]);
        // 2.3 设置纹理对象的相关信息：颜色模式、大小
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                mTextureBean.getWidth(), mTextureBean.getHeight(),
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        // 2.4 纹理过滤参数设置
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        // 2.5 解绑当前纹理，避免后续无关的操作影响了纹理内容
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private void bindFrameBufferInfo() {
        // 1. 绑定FrameBuffer到当前的绘制环境上
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
        // 2. 将纹理对象挂载到FrameBuffer上，存储颜色信息
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, mTexture[0], 0);
    }

    private void drawTexture() {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean.getTextureId());
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }

    private void unbindFrameBufferInfo() {
        // 解绑FrameBuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void deleteEnv() {
        GLES20.glDeleteFramebuffers(1, mFrameBuffer, 0);
        GLES20.glDeleteTextures(1, mTexture, 0);
    }
}

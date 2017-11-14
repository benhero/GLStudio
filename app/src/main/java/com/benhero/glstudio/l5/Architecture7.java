package com.benhero.glstudio.l5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.util.LoggerConfig;
import com.benhero.glstudio.util.ShaderHelper;
import com.benhero.glstudio.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 纹理绘制
 *
 * @author benhero
 */
public class Architecture7 extends BaseRenderer {
    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            // 纹理坐标：2个分量，S和T坐标
            "attribute vec2 a_texCoord;\n" +
            "varying vec2 v_texCoord;\n" +
            "void main()\n" +
            "{\n" +
            "    v_texCoord = a_texCoord;\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_texCoord;\n" +
            // sampler2D：二维纹理数据的数组
            "uniform sampler2D u_TextureUnit;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_texCoord);\n" +
            "}";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private static final String A_TEX_COORD = "a_texCoord";

    private int mProgram;
    private final FloatBuffer mVertexData;
    private int aPositionLocation;
    private int uMatrixLocation;

    /**
     * 纹理坐标索引
     */
    private int aTexCoordLocation;
    /**
     *
     */
    private int uTextureUnitLocation;

    private static final int POSITION_COUNT = 4;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private final float[] projectionMatrix = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };

    /**
     * 纹理坐标
     */
    private static final float[] TEX_VERTEX = {
            0, 0,
            1, 0,
            1, 1,
            0, 1,
    };
    /**
     * 纹理坐标中每个点占的向量个数
     */
    private static final int TEX_VERTEX_COMPONENT_COUNT = 2;
    private final FloatBuffer mTexVertexBuffer;

    public Architecture7(Context context) {
        super(context);
        mVertexData = ByteBuffer
                .allocateDirect(POSITION_COUNT * POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
        int vertexShader = ShaderHelper.compileVertexShader(VERTEX_SHADER);
        int fragmentShader = ShaderHelper.compileFragmentShader(FRAGMENT_SHADER);

        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }

        GLES20.glUseProgram(mProgram);

        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);

        // 纹理索引
        aTexCoordLocation = GLES20.glGetAttribLocation(mProgram, A_TEX_COORD);
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        initTextureInfo();
    }

    private void initTextureInfo() {
        for (GLImageView imageView : mGLImageViews) {
            TextureHelper.TextureBean textureBean = TextureHelper.loadTexture(mContext, imageView.getResId());
            imageView.setTextureId(textureBean.getTextureId());
            imageView.setWidth(textureBean.getWidth());
            imageView.setHeight(textureBean.getHeight());
        }
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // 比例：长：短
        float aspectRatioX;
        float aspectRatioY;
        // 换算成1.0f的基准边长
        int standard;
        if (width > height) {
            standard = height;
            aspectRatioX = (float) width / (float) height;
            aspectRatioY = 1;
        } else {
            standard = width;
            aspectRatioX = 1;
            aspectRatioY = (float) height / (float) width;
        }
        Matrix.orthoM(projectionMatrix, 0, -aspectRatioX, aspectRatioX, -aspectRatioY, aspectRatioY, -1f, 1f);

        for (GLImageView view : mGLImageViews) {
            // 坐标
            view.setXGL((view.getX() - width / 2) / (width / 2) * aspectRatioX);
            view.setYGL((height - view.getY() * 2) / height * aspectRatioY);
            // 宽高
            view.setWidthGL(view.getWidth() / (standard / 2));
            view.setHeightGL(view.getHeight() / (standard / 2));
            // 绘制范围
            view.updatePosition();
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (GLImageView view : mGLImageViews) {
            drawTexture(view);
        }
    }

    private void drawTexture(GLImageView view) {
        mVertexData.clear();
        mVertexData.put(view.getPosition());

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // 纹理单元：在OpenGL中，纹理不是直接绘制到片段着色器上，而是通过纹理单元去保存纹理

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, view.getTextureId());

        // 将纹理单元传递片段着色器的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }
}

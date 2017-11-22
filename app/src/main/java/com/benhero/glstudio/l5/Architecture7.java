package com.benhero.glstudio.l5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.benhero.glstudio.base.BaseRenderer;
import com.benhero.glstudio.base.GLAnimation;
import com.benhero.glstudio.base.GLImageView;
import com.benhero.glstudio.base.GLTranslateAnimation;
import com.benhero.glstudio.util.LoggerConfig;
import com.benhero.glstudio.util.ShaderHelper;
import com.benhero.glstudio.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.benhero.glstudio.base.GLConstants.BYTES_PER_FLOAT;
import static com.benhero.glstudio.base.GLConstants.COLOR_COMPONENT_COUNT;
import static com.benhero.glstudio.base.GLConstants.COLOR_COUNT;
import static com.benhero.glstudio.base.GLConstants.POSITION_COMPONENT_COUNT;
import static com.benhero.glstudio.base.GLConstants.POSITION_COUNT;
import static com.benhero.glstudio.base.GLConstants.TEX_VERTEX_COMPONENT_COUNT;

/**
 * 纹理绘制
 *
 * @author Benhero
 */
public class Architecture7 extends BaseRenderer {
    private static final String VERTEX_SHADER = "" +
            "uniform mat4 u_Matrix;\n" +
            "uniform mat4 u_Position_Matrix;\n" +
            "attribute vec4 a_Position;\n" +
            // 纹理坐标：2个分量，S和T坐标
            "attribute vec2 a_texCoord;\n" +
            "varying vec2 v_texCoord;\n" +
            "attribute vec4 a_color;\n" +
            "varying vec4 v_color;\n" +
            "void main()\n" +
            "{\n" +
            "    v_texCoord = a_texCoord;\n" +
            "    v_color = a_color;\n" +
            "    gl_Position = u_Matrix * u_Position_Matrix * a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 v_texCoord;\n" +
            "varying vec4 v_color;\n" +
            // sampler2D：二维纹理数据的数组
            "uniform sampler2D u_TextureUnit;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = v_color * texture2D(u_TextureUnit, v_texCoord);\n" +
            "}";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_POSITION_MATRIX = "u_Position_Matrix";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private static final String A_TEX_COORD = "a_texCoord";
    private static final String A_COLOR = "a_color";

    private int mProgram;
    private final FloatBuffer mVertexData;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int aColorLocation;
    private int uPositionMatrixLocation;

    /**
     * 纹理坐标索引
     */
    private int aTexCoordLocation;
    /**
     * 纹理裁剪范围索引
     */
    private int uTextureUnitLocation;

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
            0, 0,
            1, 0,
            1, 1,
            0, 1,
    };
    private final FloatBuffer mTexVertexBuffer;

    private FloatBuffer mColBuffer;

    private long mStartTime;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private float mAspectRatioX;
    private float mAspectRatioY;
    private int mStandardSize;

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

        mColBuffer = ByteBuffer.allocateDirect(COLOR_COUNT * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
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
        uPositionMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_POSITION_MATRIX);

        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);

        // 纹理索引
        aTexCoordLocation = GLES20.glGetAttribLocation(mProgram, A_TEX_COORD);
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        mColBuffer.position(0);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mColBuffer);
        GLES20.glEnableVertexAttribArray(aColorLocation);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        initTextureInfo();

        // 开启纹理透明混合，这样才能绘制透明图片。使用这两句语句就可以在渲染时将PNG图片的背景透明化。注意PNG必须是32位的。如果不使用这两句，PNG图片则是显示黑色背景。
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        mStartTime = System.currentTimeMillis();
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
        // 换算成1.0f的基准边长
        if (width > height) {
            mStandardSize = height;
            mAspectRatioX = (float) width / (float) height;
            mAspectRatioY = 1;
        } else {
            mStandardSize = width;
            mAspectRatioX = 1;
            mAspectRatioY = (float) height / (float) width;
        }
        Matrix.orthoM(mProjectionMatrix, 0, -mAspectRatioX, mAspectRatioX, -mAspectRatioY, mAspectRatioY, -1f, 1f);

        mSurfaceWidth = width;
        mSurfaceHeight = height;
        updatePosition();
    }

    private void updatePosition() {
        for (GLImageView view : mGLImageViews) {
            // 坐标
            view.setXGL(xPositionToGL(view.getX()));
            view.setYGL(yPositionToGL(view.getY()));
            // 宽高
            view.setWidthGL(view.getWidth() / (mStandardSize / 2));
            view.setHeightGL(view.getHeight() / (mStandardSize / 2));
            // 绘制范围
            view.resetMatrix();
            Matrix.scaleM(view.getPositionMatrix(), 0, view.getWidthGL(), view.getHeightGL(), 1);
            view.initialize(mSurfaceWidth, mSurfaceHeight, mAspectRatioX, mAspectRatioY);
        }
    }

    /**
     * x坐标转换到GL坐标系上的位置
     */
    private float xPositionToGL(float x) {
        return (x - mSurfaceWidth / 2) / (mSurfaceWidth / 2) * mAspectRatioX;
    }

    /**
     * y坐标转换到GL坐标系上的位置
     */
    private float yPositionToGL(float y) {
        return (mSurfaceHeight - y * 2) / mSurfaceHeight * mAspectRatioY;
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
        mVertexData.position(0);

        GLAnimation glAnimation = view.getGLAnimation();
        if (glAnimation != null) {
            view.resetMatrix();
            handleAnimation(view, glAnimation);
        }
        mColBuffer.clear();
        mColBuffer.put(view.getAlphas());
        mColBuffer.position(0);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);

        GLES20.glUniformMatrix4fv(uPositionMatrixLocation, 1, false,
                view.getPositionMatrix(), 0);

        // 纹理单元：在OpenGL中，纹理不是直接绘制到片段着色器上，而是通过纹理单元去保存纹理

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, view.getTextureId());

        // 将纹理单元传递片段着色器的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }

    /**
     * 处理动画
     */
    private void handleAnimation(GLImageView view, GLAnimation animation) {
        animation.getTransformation(System.currentTimeMillis(), view);
        if (animation instanceof GLTranslateAnimation) {
//                view.setX(currentX);
//                view.setY(currentY);
//
//                // 坐标
//                view.setXGL(xPositionToGL(view.getX()));
//                view.setYGL(yPositionToGL(view.getY()));
//                // 宽高
//                view.setWidthGL(view.getWidth() / (mStandardSize / 2));
//                view.setHeightGL(view.getHeight() / (mStandardSize / 2));
        }
    }

}
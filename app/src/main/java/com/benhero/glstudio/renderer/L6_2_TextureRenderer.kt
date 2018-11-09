package com.benhero.glstudio.renderer

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.R
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.ProjectionMatrixHelper
import com.benhero.glstudio.util.TextureHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 多纹理绘制
 *
 * @author Benhero
 */
class L6_2_TextureRenderer(context: Context) : BaseRenderer(context) {
    companion object {
        private val VERTEX_SHADER = """
                uniform mat4 u_Matrix;
                attribute vec4 a_Position;
                attribute vec2 a_TexCoord;
                varying vec2 v_TexCoord;
                void main() {
                    v_TexCoord = a_TexCoord;
                    gl_Position = u_Matrix * a_Position;
                }
        """
        private val FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                void main() {
                    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);
                }
                """

        private val POSITION_COMPONENT_COUNT = 2

        private val POINT_DATA2 = floatArrayOf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f)

        private val POINT_DATA = floatArrayOf(2 * -0.5f, -0.5f * 2, 2 * -0.5f, 0.5f * 2, 2 * 0.5f, 0.5f * 2, 2 * 0.5f, -0.5f * 2)

        /**
         * 纹理坐标
         */
        private val TEX_VERTEX = floatArrayOf(0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f)

        /**
         * 纹理坐标中每个点占的向量个数
         */
        private val TEX_VERTEX_COMPONENT_COUNT = 2
    }

    private val mVertexData: FloatBuffer
    private val mVertexData2: FloatBuffer

    private var uTextureUnitLocation: Int = 0
    private val mTexVertexBuffer: FloatBuffer
    /**
     * 纹理数据
     */
    private var mTextureBean: TextureHelper.TextureBean? = null
    private var mTextureBean2: TextureHelper.TextureBean? = null
    private var mProjectionMatrixHelper: ProjectionMatrixHelper? = null
    private var mAPositionLocation: Int = 0

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mVertexData2 = BufferUtil.createFloatBuffer(POINT_DATA2)
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER)

        mAPositionLocation = getAttrib("a_Position")
        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")
        // 纹理坐标索引
        val aTexCoordLocation = getAttrib("a_TexCoord")
        uTextureUnitLocation = getUniform("u_TextureUnit")
        // 纹理数据
        mTextureBean = TextureHelper.loadTexture(context, R.drawable.pikachu)
        mTextureBean2 = TextureHelper.loadTexture(context, R.drawable.tuzki)

        // 加载纹理坐标
        mTexVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer)
        GLES20.glEnableVertexAttribArray(aTexCoordLocation)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND)
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mProjectionMatrixHelper!!.enable(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
        drawPikachu()
        drawTuzki()
    }

    private fun drawPikachu() {
        mVertexData.position(0)
        GLES20.glVertexAttribPointer(mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(mAPositionLocation)

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean!!.textureId)
        GLES20.glUniform1i(uTextureUnitLocation, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }

    private fun drawTuzki() {
        mVertexData2.position(0)
        GLES20.glVertexAttribPointer(mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData2)
        GLES20.glEnableVertexAttribArray(mAPositionLocation)

        // 绑定新的纹理ID到已激活的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBean2!!.textureId)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }
}

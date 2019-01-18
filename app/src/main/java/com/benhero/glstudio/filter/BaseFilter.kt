package com.benhero.glstudio.filter

import android.content.Context
import android.opengl.GLES20
import com.benhero.glstudio.util.*
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

/**
 * 基础滤镜
 *
 * @author Benhero
 * @date   2018/11/28
 */
open class BaseFilter(val context: Context, private val vertexShader: String = VERTEX_SHADER, private val fragmentShader: String = FRAGMENT_SHADER) {
    companion object {
        const val VERTEX_SHADER = """
                uniform mat4 u_Matrix;
                attribute vec4 a_Position;
                attribute vec2 a_TexCoord;
                varying vec2 v_TexCoord;
                void main() {
                    v_TexCoord = a_TexCoord;
                    gl_Position = u_Matrix * a_Position;
                }
                """
        const val FRAGMENT_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                void main() {
                    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);
                }
                """

        private const val POSITION_COMPONENT_COUNT = 2

        private val POINT_DATA = floatArrayOf(-1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f)

        /**
         * 纹理坐标
         */
        private val TEX_VERTEX = floatArrayOf(0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f)

        /**
         * 纹理坐标中每个点占的向量个数
         */
        private const val TEX_VERTEX_COMPONENT_COUNT = 2
    }

    private val mVertexData: FloatBuffer

    private var uTextureUnitLocation: Int = 0
    private val mTexVertexBuffer: FloatBuffer
    /**
     * 纹理数据
     */
    var textureBean: TextureHelper.TextureBean? = null
    private var projectionMatrixHelper: ProjectionMatrixHelper? = null

    var program = 0

    init {
        mVertexData = BufferUtil.createFloatBuffer(POINT_DATA)
        mTexVertexBuffer = BufferUtil.createFloatBuffer(TEX_VERTEX)
    }


    open fun onCreated() {
        makeProgram(vertexShader, fragmentShader)
        val aPositionLocation = getAttrib("a_Position")
        projectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")
        // 纹理坐标索引
        val aTexCoordLocation = getAttrib("a_TexCoord")
        uTextureUnitLocation = getUniform("u_TextureUnit")

        mVertexData.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, mVertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // 加载纹理坐标
        mTexVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer)
        GLES20.glEnableVertexAttribArray(aTexCoordLocation)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND)
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA)
    }

    open fun onSizeChanged(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        projectionMatrixHelper!!.enable(width, height)
    }

    open fun onDraw() {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
        // 纹理单元：在OpenGL中，纹理不是直接绘制到片段着色器上，而是通过纹理单元去保存纹理

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureBean?.textureId ?: 0)

        // 将纹理单元传递片段着色器的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.size / POSITION_COMPONENT_COUNT)
    }

    open fun onDestroy() {
        GLES20.glDeleteProgram(program)
        program = 0
    }

    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected fun makeProgram(vertexShader: String, fragmentShader: String) {
        // 步骤1：编译顶点着色器
        val vertexShaderId = ShaderHelper.compileVertexShader(vertexShader)
        // 步骤2：编译片段着色器
        val fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader)
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        program = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId)

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program)
        }

        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program)
    }

    protected fun getUniform(name: String): Int {
        return GLES20.glGetUniformLocation(program, name)
    }

    protected fun getAttrib(name: String): Int {
        return GLES20.glGetAttribLocation(program, name)
    }
}
package com.benhero.glstudio.base

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.benhero.glstudio.util.BufferUtil
import com.benhero.glstudio.util.LoggerConfig
import com.benhero.glstudio.util.ShaderHelper
import java.nio.ByteBuffer

/**
 * GL渲染基础类
 *
 * @author Benhero
 */
abstract class BaseRenderer(val context: Context) : GLSurfaceView.Renderer {
    protected var program = 0
    public var rendererCallback: RendererCallback? = null
    public var isReadCurrentFrame = false

    /**
     * 渲染完毕的回调
     */
    interface RendererCallback {
        /**
         * 渲染完毕
         *
         * @param data   缓存数据
         * @param width  数据宽度
         * @param height 数据高度
         */
        fun onRendererDone(data: ByteBuffer, width: Int, height: Int)
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

    /**
     * 获取当前画面帧,并回调接口
     */
    protected fun readPixels(x: Int, y: Int, width: Int, height: Int) {
        if (!isReadCurrentFrame) {
            return
        }
        isReadCurrentFrame = false
        val buffer = ByteBuffer.allocate(width * height * BufferUtil.BYTES_PER_FLOAT)
        GLES20.glReadPixels(x,
                y,
                width,
                height,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, buffer)
        rendererCallback!!.onRendererDone(buffer, width, height)
    }

}
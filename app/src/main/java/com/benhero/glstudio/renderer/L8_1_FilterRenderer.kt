package com.benhero.glstudio.renderer

import android.content.Context
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.filter.BaseFilter
import com.benhero.glstudio.filter.GrayFilter
import com.benhero.glstudio.filter.InverseFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 滤镜渲染
 *
 * @author Benhero
 */
class L8_1_FilterRenderer(context: Context) : BaseRenderer(context) {
    val filterList = ArrayList<BaseFilter>()
    var drawIndex = 2

    init {
        filterList.add(BaseFilter(context))
        filterList.add(GrayFilter(context))
        filterList.add(InverseFilter(context))
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        filterList.get(drawIndex).onCreated()
    }

    override fun onSurfaceChanged(glUnused: GL10?, width: Int, height: Int) {
        filterList.get(drawIndex).onSizeChanged(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        filterList.get(drawIndex).onDraw()
    }


}

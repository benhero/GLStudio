package com.benhero.glstudio.renderer

import android.content.Context
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.filter.BaseFilter
import com.benhero.glstudio.filter.GrayFilter
import com.benhero.glstudio.filter.InverseFilter
import com.benhero.glstudio.filter.LightUpFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 滤镜渲染
 *
 * @author Benhero
 */
class L8_1_FilterRenderer(context: Context) : BaseRenderer(context) {
    val filterList = ArrayList<BaseFilter>()
    var drawIndex = 0
    var isChanged = false
    var currentFilter: BaseFilter

    init {
        filterList.add(BaseFilter(context))
        filterList.add(GrayFilter(context))
        filterList.add(InverseFilter(context))
        filterList.add(LightUpFilter(context))
        currentFilter = filterList.get(0)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        currentFilter.onCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        currentFilter.onSizeChanged(width, height)
    }

    override fun onDrawFrame(glUnused: GL10) {
        if (isChanged) {
            currentFilter = filterList.get(drawIndex)

            filterList.forEach {
                if (it != currentFilter) {
                    it.onDestroy()
                }
            }

            currentFilter.onCreated()
            currentFilter.onSizeChanged(outputWidth, outputHeight)
            isChanged = false
        }

        currentFilter.onDraw()
    }

    override fun onClick() {
        super.onClick()
        drawIndex++
        drawIndex = if (drawIndex >= filterList.size) 0 else drawIndex
        isChanged = true
    }

}

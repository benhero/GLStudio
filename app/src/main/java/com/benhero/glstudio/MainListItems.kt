package com.benhero.glstudio

import android.content.Context
import android.opengl.GLSurfaceView
import com.benhero.glstudio.renderer.*

/**
 * 主界面列表选项
 *
 * @author Benhero
 * @date   2018/10/29
 */
object MainListItems {
    val ITEMS = ArrayList<Item>()
    private val ITEM_MAP = HashMap<Class<*>, Item>()

    init {
        addItem(Item(L1_1_PointRenderer::class.java, "基础框架"))
        addItem(Item(L1_2_PointRenderer::class.java, "Point的绘制"))
        addItem(Item(P1_1_PointRenderer::class.java, "动态改变顶点位置 & 颜色"))
        addItem(Item(L2_1_ShapeRenderer::class.java, "基础图形绘制 - 点、线、三角形"))
        addItem(Item(L2_2_ShapeRenderer::class.java, "基础图形绘制 - 多边形"))
        addItem(Item(L3_1_OrthoRenderer::class.java, "正交投影变化"))
        addItem(Item(L3_2_OrthoRenderer::class.java, "正交投影变化 - 代码封装"))
        addItem(Item(L4_1_ColorfulRenderer::class.java, "渐变色"))
        addItem(Item(L4_2_ColorfulRenderer::class.java, "渐变色 - 数据传递优化"))
        addItem(Item(L5_IndexRenderer::class.java, "索引绘制"))
        addItem(Item(L6_1_TextureRenderer::class.java, "纹理渲染"))
        addItem(Item(L6_2_TextureRenderer::class.java, "多纹理渲染"))
        addItem(Item(L6_3_TextureRenderer::class.java, "蒙版遮罩"))
        addItem(Item(L7_1_FBORenderer::class.java, "FrameBuffer离屏渲染"))
        addItem(Item(L7_2_FBORenderer::class.java, "FrameBuffer离屏渲染 - RenderBuffer"))
        addItem(Item(L10_Architecture::class.java, "动画架构"))
    }

    private fun addItem(item: Item) {
        ITEMS.add(item)
        ITEM_MAP[item.className] = item
    }

    fun getIndex(className: Class<*>): Int {
        return ITEMS.indexOf(ITEM_MAP[className])
    }

    fun getClass(index: Int): Class<*> {
        return ITEMS[index].className
    }

    fun getRenderer(className: Class<*>, context: Context): GLSurfaceView.Renderer? {
        try {
            val constructor = className.getConstructor(Context::class.java)
            return constructor.newInstance(context) as GLSurfaceView.Renderer
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    class Item(val className: Class<*>, val content: String) {
        override fun toString(): String {
            return content
        }
    }
}
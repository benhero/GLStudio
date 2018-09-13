package com.benhero.glstudio;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.benhero.glstudio.renderer.KotlinRender;
import com.benhero.glstudio.renderer.L10_Architecture;
import com.benhero.glstudio.renderer.L1_1_PointRenderer;
import com.benhero.glstudio.renderer.L1_2_PointRenderer;
import com.benhero.glstudio.renderer.L2_1_ShapeRenderer;
import com.benhero.glstudio.renderer.L2_2_ShapeRenderer;
import com.benhero.glstudio.renderer.L3_1_OrthoRenderer;
import com.benhero.glstudio.renderer.L3_2_OrthoRenderer;
import com.benhero.glstudio.renderer.L4_1_ColorfulRenderer;
import com.benhero.glstudio.renderer.L4_2_ColorfulRenderer;
import com.benhero.glstudio.renderer.L5_IndexRenderer;
import com.benhero.glstudio.renderer.L6_1_TextureRenderer;
import com.benhero.glstudio.renderer.L6_2_TextureRenderer;
import com.benhero.glstudio.renderer.L7_1_FBORenderer;
import com.benhero.glstudio.renderer.L7_2_FBORenderer;
import com.benhero.glstudio.renderer.P1_1_PointRenderer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面列表选项
 *
 * @author Benhero
 */
public class MainListItems {
    public static List<Item> ITEMS = new ArrayList<>();
    private static Map<Class, Item> ITEM_MAP = new HashMap<>();

    static {
        addItem(new Item(L1_1_PointRenderer.class, "基础框架"));
        addItem(new Item(L1_2_PointRenderer.class, "Point的绘制"));
        addItem(new Item(P1_1_PointRenderer.class, "动态改变顶点位置 & 颜色"));
        addItem(new Item(L2_1_ShapeRenderer.class, "基础图形绘制 - 点、线、三角形"));
        addItem(new Item(L2_2_ShapeRenderer.class, "基础图形绘制 - 多边形"));
        addItem(new Item(L3_1_OrthoRenderer.class, "正交投影变化"));
        addItem(new Item(L3_2_OrthoRenderer.class, "正交投影变化 - 代码封装"));
        addItem(new Item(L4_1_ColorfulRenderer.class, "渐变色"));
        addItem(new Item(L4_2_ColorfulRenderer.class, "渐变色 - 数据传递优化"));
        addItem(new Item(L5_IndexRenderer.class, "索引绘制"));
        addItem(new Item(L6_1_TextureRenderer.class, "纹理渲染"));
        addItem(new Item(L6_2_TextureRenderer.class, "多纹理渲染"));
        addItem(new Item(L7_1_FBORenderer.class, "FrameBuffer离屏渲染"));
        addItem(new Item(L7_2_FBORenderer.class, "FrameBuffer离屏渲染 - RenderBuffer"));
        addItem(new Item(L10_Architecture.class, "动画架构"));
        addItem(new Item(KotlinRender.class, "使用kotlin编写"));
    }

    /**
     * 获取类对应的渲染器对象
     */
    public static GLSurfaceView.Renderer getRenderer(Class className, Context context) {
        try {
            Constructor constructor = className.getConstructor(Context.class);
            Object o = constructor.newInstance(context);
            return (GLSurfaceView.Renderer) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取类在列表中的位置
     */
    public static int getIndex(Class className) {
        return ITEMS.indexOf(ITEM_MAP.get(className));
    }

    /**
     * 获取位置所对应的类
     */
    public static Class getClass(int index) {
        return ITEMS.get(index).mClassName;
    }

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mClassName, item);
    }

    public static class Item {
        public Class mClassName;
        public String mContent;

        public Item(Class className, String content) {
            mClassName = className;
            mContent = content;
        }

        @Override
        public String toString() {
            return mContent;
        }
    }
}

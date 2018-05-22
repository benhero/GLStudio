package com.benhero.glstudio;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.benhero.glstudio.l1.PointRenderer1_1_1;
import com.benhero.glstudio.l1.PointRenderer1_1_2;
import com.benhero.glstudio.l1.ShapeRenderer1_2;
import com.benhero.glstudio.l2.IndexRenderer2_2;
import com.benhero.glstudio.l2.OrthoRenderer2_1;
import com.benhero.glstudio.l3.ColorfulRenderer3;
import com.benhero.glstudio.l4.TextureRenderer4;
import com.benhero.glstudio.l5.Architecture5;
import com.benhero.glstudio.l6.FBORenderer6;

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
    public static Map<Class, Item> ITEM_MAP = new HashMap<>();

    static {
        addItem(new Item(PointRenderer1_1_1.class, "基础框架"));
        addItem(new Item(PointRenderer1_1_2.class, "Point的绘制"));
        addItem(new Item(ShapeRenderer1_2.class, "基础图形绘制"));
        addItem(new Item(OrthoRenderer2_1.class, "正交投影变化"));
        addItem(new Item(IndexRenderer2_2.class, "索引的使用"));
        addItem(new Item(ColorfulRenderer3.class, "渐变色"));
        addItem(new Item(TextureRenderer4.class, "纹理渲染"));
        addItem(new Item(Architecture5.class, "动画架构"));
        addItem(new Item(FBORenderer6.class, "FrameBuffer"));
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

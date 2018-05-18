package com.benhero.glstudio;

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
    public static List<Item> ITEMS = new ArrayList<Item>();
    public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    static {
        addItem(new Item("0", "基础框架"));
        addItem(new Item("1", "Point的绘制"));
        addItem(new Item("2", "基础图形绘制"));
        addItem(new Item("3", "正交投影变化"));
        addItem(new Item("4", "索引的使用"));
        addItem(new Item("5", "渐变色"));
        addItem(new Item("6", "纹理渲染"));
        addItem(new Item("7", "动画架构"));
        addItem(new Item("8", "FrameBuffer"));
    }

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mId, item);
    }

    public static class Item {
        public String mId;
        public String mContent;

        public Item(String id, String content) {
            this.mId = id;
            this.mContent = content;
        }

        @Override
        public String toString() {
            return mContent;
        }
    }
}

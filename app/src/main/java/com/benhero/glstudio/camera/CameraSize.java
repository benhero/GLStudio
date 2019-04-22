package com.benhero.glstudio.camera;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Camera的Size。从parameters中获取
 */
public class CameraSize implements Comparable<CameraSize> {
    private final int mWidth;
    private final int mHeight;

    public CameraSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public CameraSize inverse() {
        return new CameraSize(mHeight, mWidth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CameraSize cameraSize = (CameraSize) o;

        if (mWidth != cameraSize.mWidth) return false;
        return mHeight == cameraSize.mHeight;
    }

    @Override
    public int hashCode() {
        int result = mWidth;
        result = 31 * result + mHeight;
        return result;
    }

    @Override
    public int compareTo(@NonNull CameraSize another) {
        return this.mHeight * this.mWidth - another.mWidth * another.mHeight;
    }

    @Override
    public String toString() {
        return mWidth + "x" + mHeight;
    }


    /**
     * 这个map是根据 比例AspectRadio来存放，对应比例的尺寸的。同时，尺寸按照大小排列
     */
    public static class ISizeMap {
        private final HashMap<AspectRatio, SortedSet<CameraSize>> mRatioSizeSets = new HashMap<>();

        public boolean add(CameraSize size) {
            for (AspectRatio aspectRatio : mRatioSizeSets.keySet()) {
                if (aspectRatio.matches(size)) {
                    SortedSet<CameraSize> cameraSizes = mRatioSizeSets.get(aspectRatio);
                    if (cameraSizes.contains(size)) {
                        return false;
                    } else {
                        cameraSizes.add(size);
                        return true;
                    }
                }
            }

            //没有找到当前的尺寸的话
            SortedSet<CameraSize> sizes = new TreeSet<>();
            sizes.add(size);
            mRatioSizeSets.put(AspectRatio.of(size.getWidth(), size.getHeight()), sizes);
            return true;
        }

        public void remove(AspectRatio ratio) {
            mRatioSizeSets.remove(ratio);
        }

        public Set<AspectRatio> ratios() {
            return mRatioSizeSets.keySet();
        }

        public SortedSet<CameraSize> sizes(AspectRatio ratio) {
            return mRatioSizeSets.get(ratio);
        }

        public void clear() {
            mRatioSizeSets.clear();
        }

        public boolean isEmpty() {
            return mRatioSizeSets.isEmpty();
        }
    }
}
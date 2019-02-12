package com.benhero.glstudio.video

import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.text.TextUtils
import android.util.Log
import java.util.*

/**
 * 视频数据信息类
 *
 * @author Benhero
 * @date   2019/2/11
 */
class VideoInfo(val path: String) {
    init {
        initMetadata()
    }

    /**
     * 视频时长
     */
    var duration: Int = 0
        private set
    /**
     * 原始视频附带的旋转角度
     */
    var degrees: Int = 0
        private set
    /**
     * 比特率
     */
    var bitRate: Int = 0
        private set
    /**
     * 视频宽 - 数据存储：可能由于旋转角度导致和展示上看到的不一致
     */
    var width: Int = 0
        private set
    /**
     * 视频高 - 数据存储：可能由于旋转角度导致和展示上看到的不一致
     */
    var height: Int = 0
        private set
    /**
     * 视频宽 - 展示播放时
     */
    var displayWidth: Int = 0
        private set
    /**
     * 视频高 - 展示播放时
     */
    var displayHeight: Int = 0
        private set
    /**
     * 地理位置信息
     */
    var location = arrayOf<String>()
        private set

    /**
     * 视觉上和数据存储宽高是否需要转置
     */
    val isDisplayRotate: Boolean
        get() = degrees == 90 || degrees == 270

    /**
     * 初始化视频信息
     */
    fun initMetadata() {
        if (TextUtils.isEmpty(path)) {
            return
        }

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(path)
            degrees = getInteger(retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION))
            duration = getInteger(retriever.extractMetadata(METADATA_KEY_DURATION))
            bitRate = getInteger(retriever.extractMetadata(METADATA_KEY_BITRATE))
            width = getInteger(retriever.extractMetadata(METADATA_KEY_VIDEO_WIDTH))
            height = getInteger(retriever.extractMetadata(METADATA_KEY_VIDEO_HEIGHT))
            displayWidth = if (isDisplayRotate) height else width
            displayHeight = if (isDisplayRotate) width else height
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d(TAG, toString())
    }

    internal fun getInteger(value: String): Int {
        return if (TextUtils.isEmpty(value)) {
            0
        } else Integer.valueOf(value)
    }


    override fun toString(): String {
        return ("Path:" + path
                + "\n Location:" + Arrays.toString(location) + ", mBitRate:" + bitRate
                + "\n Width:" + width + ", Height:" + height
                + "\n Degrees:" + degrees + ", Duration:" + duration)
    }

    companion object {


        private val TAG = "VideoInfo"
    }
}
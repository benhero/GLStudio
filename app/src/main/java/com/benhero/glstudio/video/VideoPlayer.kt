package com.benhero.glstudio.video

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message


/**
 * 视频播放器
 *
 * @author Benhero
 * @date 2019/2/11
 */
class VideoPlayer : MediaPlayer(), MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    companion object {
        private const val MSG_UPDATE = 101
        private const val UPDATE_INTERVAL = 16
    }
    private var listener: PlayerListener? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_UPDATE -> {
                    sendEmptyMessageDelayed(MSG_UPDATE, UPDATE_INTERVAL.toLong())
                    if (listener != null) {
                        listener!!.onPlayerUpdate(1.0f * currentPosition / duration)
                    }
                }
                else -> {
                }
            }
        }
    }

    init {
        setOnCompletionListener(this)
        setOnSeekCompleteListener(this)
    }

    fun setListener(listener: PlayerListener) {
        this.listener = listener
    }

    override fun onCompletion(mp: MediaPlayer) {
        if (!mp.isPlaying) {
            handler.removeCallbacksAndMessages(null)
            if (listener != null) {
                listener!!.onPlayerStop()
            }
            return
        }
        handler.sendEmptyMessage(MSG_UPDATE)
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        if (!mp.isPlaying) {
            handler.removeCallbacksAndMessages(null)
            return
        }
        handler.sendEmptyMessage(MSG_UPDATE)
    }

    @Throws(IllegalStateException::class)
    override fun start() {
        super.start()
        handler.sendEmptyMessage(MSG_UPDATE)
        if (listener != null) {
            listener!!.onPlayerStart()
        }
    }

    @Throws(IllegalStateException::class)
    override fun pause() {
        super.pause()
        handler.removeCallbacksAndMessages(null)
        if (listener != null) {
            listener!!.onPlayerPause()
        }
    }

    @Throws(IllegalStateException::class)
    override fun stop() {
        super.stop()
        handler.removeCallbacksAndMessages(null)
        if (listener != null) {
            listener!!.onPlayerStop()
        }
    }

    override fun seekTo(msec: Long, mode: Int) {
        super.seekTo(msec, mode)
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * 播放器监听器
     */
    interface PlayerListener {
        fun onPlayerStart()

        fun onPlayerStop()

        fun onPlayerPause()

        fun onPlayerUpdate(percent: Float)
    }
}

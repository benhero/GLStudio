package com.benhero.glstudio

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.benhero.glstudio.renderer.L10_1_VideoRenderer
import com.benhero.glstudio.util.FileUtil
import com.benhero.glstudio.util.TimeUtil
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*

/**
 * 视频播放页面
 *
 * @author Benhero
 * @date   2019/1/30
 */
class VideoActivity : Activity(), View.OnClickListener, VideoPlayer.PlayerListener {
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var player: VideoPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var textProgress: TextView
    private lateinit var textDuration: TextView

    private val REQUEST_PICK_VIDEO = 1124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        glSurfaceView = findViewById<View>(R.id.activity_video_surfaceView) as GLSurfaceView
        seekBar = findViewById(R.id.activity_video_seekbar)
        textProgress = findViewById(R.id.activity_video_text_progress)
        textDuration = findViewById(R.id.activity_video_text_duration)
        player = VideoPlayer()
        player.setListener(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(L10_1_VideoRenderer(this, player))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        glSurfaceView.setOnClickListener(this)
        checkPermission()
    }

    private fun checkPermission() {
        TedPermission.with(this@VideoActivity)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        pickVideo()
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        Toast.makeText(this@VideoActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast
                                .LENGTH_SHORT).show()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at " + "[Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }

    private fun pickVideo() {
        if (false) {
            onVideoPicked("/storage/emulated/0/DCIM/Camera/VID_20181016_145713.mp4")
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            startActivityForResult(intent, REQUEST_PICK_VIDEO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                handleImage(data)
            } else {
                finish()
            }
        }
    }

    private fun handleImage(intent: Intent) {
        val clipData = intent.clipData
        if (clipData == null) {
            val uri = intent.data
            if (uri != null && !TextUtils.isEmpty(uri.path)) {
                val path: String?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    path = FileUtil.getPath(this.applicationContext, uri)
                } else {
                    path = uri.path
                }
                onVideoPicked(path)
                return
            }
            return
        }
        for (i in 0 until clipData.itemCount) {
            val contentURI = clipData.getItemAt(i).uri
            val result: String?
            val cursor = contentResolver.query(contentURI, null, null, null, null)
            if (cursor == null) {
                result = contentURI.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            if (result != null) {
                onVideoPicked(result)
            }
        }
    }

    private fun onVideoPicked(path: String?) {
        if (path == null) {
            return
        }
        player.setDataSource(path)
        player.prepare()
        player.isLooping = true
        player.setOnPreparedListener {
            player.start()
            textDuration.text = TimeUtil.formatVideoTime(player.duration.toLong())
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()
    }

    override fun onClick(v: View?) {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.start()
        }
    }

    override fun onPlayerStart() {
    }

    override fun onPlayerStop() {
    }

    override fun onPlayerPause() {
    }

    override fun onPlayerUpdate(percent: Float) {
        seekBar.progress = (percent * 100).toInt()
        textProgress.text = TimeUtil.formatVideoTime(player.currentPosition.toLong())
    }
}
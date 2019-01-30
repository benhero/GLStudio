package com.benhero.glstudio

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import com.benhero.glstudio.util.FileUtil
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*

/**
 * 视频播放页面
 *
 * @author Benhero
 * @date   2019/1/30
 */
class VideoActivity : Activity() {
    private lateinit var glSurfaceView: SurfaceView
    private lateinit var player: MediaPlayer

    private val REQUEST_PICK_VIDEO = 1124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        glSurfaceView = findViewById<View>(R.id.activity_video_surfaceView) as SurfaceView
        player = MediaPlayer()
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
        glSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                player.setDisplay(holder)
                player.isLooping = true
                player.start()
            }
        })
    }
}
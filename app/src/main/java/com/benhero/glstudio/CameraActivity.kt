package com.benhero.glstudio

import android.Manifest
import android.app.Activity
import android.graphics.drawable.Animatable
import android.hardware.Camera
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.benhero.glstudio.camera.CameraApi14
import com.benhero.glstudio.renderer.L11_1_CameraRenderer
import com.benhero.glstudio.video.AspectFrameLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*
import java.util.concurrent.Executors

/**
 * 相机界面
 */
class CameraActivity : Activity(), View.OnClickListener {
    /**
     * 用于解决不同长宽比视频的适配问题，让GL绘制尽量处于填充满容器的状态
     */
    private lateinit var contentWrapper: AspectFrameLayout
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var switchCamera: View
    private lateinit var renderer: L11_1_CameraRenderer
    private var isFront = true
    private val camera = CameraApi14()
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        checkPermission()

        contentWrapper = findViewById(R.id.activity_camera_content_wrapper)
        glSurfaceView = findViewById(R.id.activity_camera_surfaceView)
        glSurfaceView.setEGLContextClientVersion(2)
        renderer = L11_1_CameraRenderer(this, camera)
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        glSurfaceView.setOnClickListener(this)
        switchCamera = findViewById(R.id.activity_camera_switch)
        switchCamera.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        openCamera()
    }

    override fun onStop() {
        super.onStop()
        camera.close()
    }

    private fun openCamera() {
        camera.open(if (isFront) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK)
        camera.preview()
        glSurfaceView.requestRender()
    }

    private fun switchCamera() {
        isFront = !isFront
        openCamera()
        renderer.switchCamera()
    }

    override fun onClick(v: View?) {
        when (v) {
            switchCamera -> {
                executor.execute {
                    switchCamera()
                }

                val imageView = v as ImageView
                val drawable = imageView.drawable
                if (drawable is Animatable) drawable.start()
            }
        }
    }

    private fun checkPermission() {
        TedPermission.with(this@CameraActivity)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        Toast.makeText(this@CameraActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast
                                .LENGTH_SHORT).show()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at " + "[Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check()
    }
}
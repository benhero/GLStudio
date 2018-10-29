package com.benhero.glstudio

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.benhero.glstudio.base.*
import com.benhero.glstudio.renderer.L10_Architecture
import com.benhero.glstudio.renderer.L6_2_TextureRenderer
import com.benhero.glstudio.renderer.L7_1_FBORenderer
import com.benhero.glstudio.renderer.L7_2_FBORenderer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jayfeng.lesscode.core.BitmapLess
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 * 主界面
 *
 * @author Benhero
 */
class MainActivity : Activity(), AdapterView.OnItemClickListener {
    private lateinit var root: ViewGroup
    private lateinit var listView: ListView
    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById<View>(R.id.main_root) as ViewGroup
        listView = findViewById<View>(R.id.main_list) as ListView
        val adapter = ArrayAdapter<MainListItems.Item>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, MainListItems.ITEMS)
        listView.adapter = adapter
        listView.onItemClickListener = this
        // 自动点击
        val position = MainListItems.getIndex(L6_2_TextureRenderer::class.java)
        listView.performItemClick(adapter.getView(position, null, listView),
                position, adapter.getItemId(position))

        TedPermission.with(this@MainActivity)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        //                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        Toast.makeText(this@MainActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast
                                .LENGTH_SHORT).show()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at " + "[Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }

    override fun onResume() {
        super.onResume()
        if (glSurfaceView != null) {
            glSurfaceView!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (glSurfaceView != null) {
            glSurfaceView!!.onPause()
        }
    }

    override fun onBackPressed() {
        if (glSurfaceView != null) {
            // 展示了GLSurfaceView，则删除ListView之外的其余控件
            var childCount = root.childCount
            var i = 0
            while (i < childCount) {
                if (root.getChildAt(i) !== listView) {
                    root.removeViewAt(i)
                    childCount--
                    i--
                }
                i++
            }
            glSurfaceView = null
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        glSurfaceView = GLSurfaceView(this)
        root.addView(glSurfaceView)
        glSurfaceView!!.setEGLContextClientVersion(2)
        glSurfaceView!!.setEGLConfigChooser(false)

        val clickClass = MainListItems.getClass(position)
        val renderer = MainListItems.getRenderer(clickClass, this)
        if (renderer == null) {
            Toast.makeText(this, "反射构建渲染器失败", Toast.LENGTH_SHORT).show()
            return
        }

        if (clickClass == L10_Architecture::class.java) {
            chooseArchitecture(renderer as L10_Architecture)
        } else if (clickClass == L7_1_FBORenderer::class.java || clickClass == L7_2_FBORenderer::class.java) {
            readCurrentFrame(renderer as BaseRenderer)
        }

        glSurfaceView!!.setRenderer(renderer)
        glSurfaceView!!.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        glSurfaceView!!.setOnClickListener { glSurfaceView!!.requestRender() }
    }

    private fun chooseArchitecture(renderer: L10_Architecture) {
        val imageView = GLImageView()
        imageView.resId = R.drawable.tuzki
        imageView.x = 400f
        imageView.y = 400f
        imageView.alpha = 1f
        renderer.addGLImageView(imageView)

        val imageView2 = GLImageView()
        imageView2.resId = R.mipmap.ic_launcher
        imageView2.x = 200f
        imageView2.y = 1920f
        imageView2.alpha = 1f
        //        renderer.addGLImageView(imageView2);

        // 透明度
        val alphaAnimation = GLAlphaAnimation()
        alphaAnimation.fromAlpha = 0.5f
        alphaAnimation.toAlpha = 1f
        alphaAnimation.interpolator = DecelerateInterpolator()
        val now = System.currentTimeMillis()

        // 位移
        val translateAnimation = GLTranslateAnimation()
        translateAnimation.fromX = 500f
        translateAnimation.fromY = 0f
        translateAnimation.toX = 500f
        translateAnimation.toY = 1400f
        translateAnimation.interpolator = DecelerateInterpolator(2f)

        // 缩放
        val scaleAnimation = GLScaleAnimation()
        scaleAnimation.fromX = 0.5f
        scaleAnimation.fromY = 0.5f
        scaleAnimation.toX = 2.0f
        scaleAnimation.toY = 2.0f
        scaleAnimation.interpolator = DecelerateInterpolator(2f)

        // 旋转
        val rotateAnimation = GLRotateAnimation()
        rotateAnimation.fromDegrees = 0f
        rotateAnimation.toDegrees = 360f
        rotateAnimation.interpolator = OvershootInterpolator()

        val set = GLAnimationSet()
        set.addAnimation(translateAnimation)
        set.addAnimation(rotateAnimation)
        set.addAnimation(alphaAnimation)
        set.addAnimation(scaleAnimation)
        imageView.glAnimation = set

        set.startTime = now
        set.duration = 3000
        set.setListener(object : GLAnimationListener {
            override fun onStart() {}

            override fun onEnd() {}

            override fun onProgress(percent: Float) {
                glSurfaceView!!.requestRender()
            }
        })
        imageView2.glAnimation = alphaAnimation
    }

    @SuppressLint("SdCardPath")
    private fun readCurrentFrame(renderer: BaseRenderer) {
        val imageView = ImageView(this)
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        root.addView(imageView, params)
        renderer.rendererCallback = object : BaseRenderer.RendererCallback {
            override fun onRendererDone(data: ByteBuffer, width: Int, height: Int) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(data)
                val destFile = File("/sdcard/A/test"
                        //+ String.valueOf(System.currentTimeMillis())
                        + ".jpg")
                try {
                    File("/sdcard/A").mkdirs()
                    destFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Thread(Runnable {
                    BitmapLess.`$save`(bitmap, Bitmap.CompressFormat.JPEG, 100, destFile)
                    imageView.post { imageView.setImageBitmap(BitmapFactory.decodeFile(destFile.path)) }
                }).start()
                data.clear()
            }

        }

        imageView.setOnClickListener {
            renderer.isReadCurrentFrame = true
            glSurfaceView!!.requestRender()
        }
    }

}

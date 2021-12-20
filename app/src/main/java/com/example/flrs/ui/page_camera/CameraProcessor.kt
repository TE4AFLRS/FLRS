//package com.example.flrs.ui.page_camera
//
//import android.app.Activity
//import android.hardware.camera2.CameraDevice
//import android.view.TextureView
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.fragment.app.Fragment
//import com.example.flrs.R
//import java.util.jar.Manifest
//
//class CameraProcessor {
//    private var cameraDevice: CameraDevice? = null
//
//    private val REQUEST_CAMERA_PERMISSION = 1
//
//    lateinit var fragment:Fragment
//    private val textureView: TextureView
//
//    constructor(textureView: TextureView, activity: Activity) {
//        this.textureView = textureView
//        this.fragment = fragment
//    }
//
//    private fun showDialogCameraPermission() {
//        if(!ActivityCompat.shouldShowRequestPermissionRationale(fragment, Manifest.permission.CAMERA)) {
//            ActivityCompat.requestPermissions(
//                activity, arrayOf(Manifest.permission.CAMERA),
//                REQUEST_CAMERA_PERMISSION
//            )
//            return
//        }
//
//        AlertDialog.Builder(activity)
//            .setMessage("R string request permission")
//            .setPositiveButton(R.string.ok) { dialog, which ->
//                ActivityCompat.requestPermissions(
//                    activity, arrayOf(Manifest.permission.CAMERA),
//                    REQUEST_CAMERA_PERMISSION
//                )
//            }
//            .setNegativeButton(R.string.cancel) { dialog, which ->  }
//            .create()
//    }
//
//    fun setup() {
//        // カメラ搭載の有無をチェック
//        if (activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) === false) return
//
//        // カメラのアクセス権限を確認して、なければ許可を求める
//        val currentPermssion = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
//        if (currentPermssion != PackageManager.PERMISSION_GRANTED) {
//            showDialogCameraPermission()
//            return
//        }
//
//        // すでにカメラが起動していたら何もしない
//        if (cameraDevice !== null) {
//            return
//        }
//
//        // 背面カメラの取得
//    }
//
//}
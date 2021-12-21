package com.example.flrs.ui.page_camera

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_ROTATE_180
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.TextureView
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.flrs.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PageCameraFragment : Fragment(R.layout.fragment_page_camera) {

    companion object {
        fun newInstance() = PageCameraFragment()
    }
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var viewModel: PageCameraViewModel
    private lateinit var  imageView:ImageView
    private val CAMERA_REQUEST_CODE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageCameraViewModel::class.java)
        // TODO: Use the ViewModel
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack()
        }
        val cameraButton: Button = view.findViewById(R.id.photoButton)
        imageView= view.findViewById(R.id.imageView)
        cameraButton.setOnClickListener {
            //カメラ機能のチェック
            this.context?.packageManager?.let {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(it)?.let {
                    if (checkCameraPermission()) {
                        takePicture()
                    } else {
                        grantCameraPermission()
                    }
                }
            }
        }

    }
    private fun checkCameraPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) }
    }

    private fun grantCameraPermission() = activity?.let {
        ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
    }

    private fun takePicture() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//            addCategory(Intent.CATEGORY_DEFAULT)
//        }
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = createImageFile()

                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }


        //startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var image =data?.extras?.keySet()
        data?.extras?.get("data")?.let {
            var bitmap:Bitmap = imageRotate(data?.extras?.get("EXTRA_OUTPUT") as File,it as Bitmap)
            imageView.setImageBitmap(bitmap)
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "TemporarySaving", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    fun imageRotate(image: File, bitmap: Bitmap): Bitmap {
        var exifinterface = ExifInterface(image.absolutePath)
        val matrix:Matrix? = null

        var orientation:Int = exifinterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED)

        when (orientation){
            ExifInterface.ORIENTATION_NORMAL ->{}
            ExifInterface.ORIENTATION_ROTATE_180 -> {matrix?.postRotate(180f)}
            ExifInterface.ORIENTATION_ROTATE_90 -> {matrix?.postRotate(90f)}
            ExifInterface.ORIENTATION_ROTATE_270 -> {matrix?.postRotate(-90f)}
        }
        var rotateBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        return rotateBitmap

    }
}

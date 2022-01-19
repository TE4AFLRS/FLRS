package com.example.flrs.ui.page_camera

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import com.yalantis.ucrop.UCrop
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getCodeCacheDir
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.navigation.fragment.findNavController
import com.example.flrs.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class PageCameraFragment : Fragment(R.layout.fragment_page_camera) {

    companion object {
        fun newInstance() = PageCameraFragment()
    }

    private val REQUEST_TAKE_PHOTO = 1
    private val PIC_VAL = 2
    private lateinit var viewModel: PageCameraViewModel
    private lateinit var textView: TextView
    private  lateinit var photoButton:Button
    private val CAMERA_REQUEST_CODE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 2
    private var photoUri: Uri? = null
    private var timeStamp: String? = null
    private var bitmapUri: Uri? = null
    private lateinit var image: InputImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageCameraViewModel::class.java)
        // TODO: Use the ViewModel

//        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            findNavController().navigate(R.id.action_page_camera_fragment_to_navigation_page_register_select)
//        }

        photoButton = view.findViewById(R.id.photoButton)
        textView = view.findViewById(R.id.textView)
        takePicture()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_page_camera_fragment_to_navigation_page_register_select)
        }

    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == REQUEST_TAKE_PHOTO) {
                openCropImage(photoUri)
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (resultCode == RESULT_OK) {
                    val resultUri = UCrop.getOutput(data!!)
                    if (resultUri != null) {
                        // TODO: 画像を使う処理を書く
                        //ここにocr
                        var imagePath = imageFromPath(requireContext(),resultUri)

                        recognizeText(imagePath)

//                        val action = PageCameraFragmentDirections.actionPageCameraFragmentToPageInsertFragment(content)
//                        findNavController().navigate(action)
                    }
                } else{
                    findNavController().navigate(R.id.action_page_camera_fragment_to_navigation_page_register_select)
                }
            }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun openCropImage(uri: Uri?) {
        val fileName = "crop.jpg"
        val cropFile = File(requireContext().cacheDir, fileName)
        cropFile.let { file ->
            val cropUri = Uri.fromFile(file)
            var uCrop = UCrop.of(uri!!, cropUri)
            //uCropのオプションを設定
            val options = UCrop.Options()
            options.setToolbarTitle("画像切り出し画面")
            options.setToolbarWidgetColor(getColor(requireContext(), R.color.darkGray))
            options.setToolbarColor(getColor(requireContext(), R.color.almond))
            options.setStatusBarColor(
                getColor(
                    requireContext(),
                    R.color.almond
                )
            )
            options.setActiveControlsWidgetColor(getColor(requireContext(), R.color.almond))
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            options.setCompressionQuality(100)
            options.setHideBottomControls(false)
            options.setFreeStyleCropEnabled(true)
            uCrop = uCrop.withOptions(options)
            uCrop.start(requireContext(), this, UCrop.REQUEST_CROP)
        }
    }

    private fun imageFromPath(context: Context, uri: Uri) :InputImage {
        // [START image_from_path]
        try {
            image = InputImage.fromFilePath(context, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
        // [END image_from_path]
    }

    private fun recognizeText(image: InputImage) {


        // [START get_detector_default]
        val recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
        // [END get_detector_default]
        // [START run_detector]
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_text]
                val action = PageCameraFragmentDirections.actionPageCameraFragmentToPageInsertFragment(visionText.text)
                findNavController().navigate(action)

                // [END get_text]
                // [END_EXCLUDE]
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
//        // [END run_detector]
    }


}

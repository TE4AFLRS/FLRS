package com.example.flrs.ui.page_insert

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.room.Insert
import com.example.flrs.*
import com.example.flrs.ui.home.HomeFragment
import com.example.flrs.ui.page_camera.PageCameraFragmentDirections
import com.example.flrs.ui.page_camera.PageCameraViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class PageInsertFragment : Fragment(R.layout.fragment_page_insert) {

    companion object {
        fun newInstance() = PageInsertFragment()
    }

    private val args: PageInsertFragmentArgs by navArgs()
    private lateinit var content: String
    private lateinit var viewModel: PageInsertViewModel
    lateinit var mCategoriesDao: CategoriesDao
    lateinit var mFoodsDao: FoodsDao
    private val REQUEST_TAKE_PHOTO = 1
    private var photoUri: Uri? = null
    private var timeStamp: String? = null
    private lateinit var image: InputImage
    lateinit var currentPhotoPath: String

    private fun getAdapter(): ArrayAdapter<String> {
        var list: List<CategoryRow> = mCategoriesDao.getAll()
        return ArrayAdapter(requireContext(), R.layout.spinner_item, list.map { it.category_name })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageInsertViewModel::class.java)
        // TODO: Use the ViewModel

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_page_insert_fragment_to_navigation_page_register_select)
        }
        mCategoriesDao = FoodsDatabase.getInstance(requireContext()).categoriesDao()
        mFoodsDao = FoodsDatabase.getInstance(requireContext()).foodsDao()

        var list: List<CategoryRow> = mCategoriesDao.getAll()
        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item, list.map { it.category_name })
        val vg = view.findViewById<View>(R.id.insert_table) as ViewGroup
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val fab: FloatingActionButton = view.findViewById(R.id.insert_fab)

        try {
            if (args.flagState.isNotEmpty()) {
                takePicture()
            }
        } catch (e: InvocationTargetException) {
            print("not found args.content")
        }

        fab.setOnClickListener {
            addNewRow(vg, adapter)
        }

        saveButton.setOnClickListener {
            var flag = 1
            for (i in vg.children) {
                val name = i.findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
                if(name == ""){
                    //ポップアップを表示したい
                    val myToast = Toast.makeText(requireContext(),"食品名が未入力の物があります。\n食品名は必須入力です。",Toast.LENGTH_SHORT)
                    myToast.show()
                    flag = 1
                    break
                }else{
                    flag = 0
                }
            }
            if (flag == 0){
                for(i in vg.children){
                    val name = i.findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
                    val id = i.findViewById<Spinner>(R.id.spinner).selectedItemPosition
                    insertFoods(name, list[id].category_id)
                }
                findNavController().navigate(R.id.action_page_insert_fragment_to_navigation_home)
            }
        }


    }

    private fun addNewRow(vg: ViewGroup, adapter: ArrayAdapter<String>) {
        val table = layoutInflater.inflate(R.layout.page_insert_table_row, vg) as LinearLayout
        val row = (table.getChildAt(table.childCount - 1) as LinearLayout)
        val spinner: Spinner = row.findViewById(R.id.spinner)
        spinner.adapter = adapter
        (row.getChildAt(0) as ImageButton).setOnClickListener {
            if (table.childCount > 1) {
                vg.removeView(row)
            }
        }
    }

    @Insert
    private fun insertFoods(foodName: String, categoryId: Int) {
        val newFood = Foods(0, foodName, categoryId, LocalDate.now().toString())
        mFoodsDao.insert(newFood)
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
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(data!!)
                if (resultUri != null) {
                    // TODO: 画像を使う処理を書く
                    var imagePath = imageFromPath(requireContext(), resultUri)
//                    recognizeText()でocrをし、一要素ごとにカラムを生成してる。
                    recognizeText(imagePath)
                }
            } else {
                findNavController().navigate(R.id.action_page_insert_fragment_to_navigation_page_register_select)
            }
        }
    }

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
            options.setToolbarTitle("食品名を切り抜いてください")
            options.setToolbarWidgetColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.darkGray
                )
            )
            options.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.almond))
            options.setStatusBarColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.almond
                )
            )
            options.setActiveControlsWidgetColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.almond
                )
            )
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            options.setCompressionQuality(100)
            options.setHideBottomControls(false)
            options.setFreeStyleCropEnabled(true)
            uCrop = uCrop.withOptions(options)
            uCrop.start(requireContext(), this, UCrop.REQUEST_CROP)
        }
    }

    private fun imageFromPath(context: Context, uri: Uri): InputImage {
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
        val recognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())

        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                var list: List<CategoryRow> = mCategoriesDao.getAll()
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    list.map { it.category_name })
                val vg = view?.findViewById<View>(R.id.insert_table) as ViewGroup
                content = visionText.text
                val foodName = content.split("\n")
                for (a in foodName) {
                    addNewRow(vg, adapter)
                    ((vg.getChildAt(vg.childCount - 1) as LinearLayout).getChildAt(1) as EditText).setText(a)
                }
            }
            .addOnFailureListener { e -> }
        val recognizerTask = PseudoTask(result)
        recognizerTask.start()
        recognizerTask.join()
    }

    class PseudoTask<T>(task: Task<T>) : Thread() {
        private val task: Task<T> = task
        override fun run() {
            Tasks.await(task)
        }
    }

}
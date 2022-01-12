package com.example.flrs.ui.page_register_select

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.flrs.R
import com.example.flrs.ui.page_camera.PageCameraFragment
import com.example.flrs.ui.page_insert.PageInsertFragment

class PageRegisterSelectFragment : Fragment(R.layout.fragment_page_register_select) {

    companion object {
        fun newInstance() = PageRegisterSelectFragment()
    }

    private lateinit var viewModel: PageRegisterSelectViewModel
    private val CAMERA_PERMISSION_REQUEST_CODE = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageRegisterSelectViewModel::class.java)
//        val textView : TextView = view.findViewById(R.id.text_PageRegisterSelect)

        val buttonManual : Button = view.findViewById(R.id.button_manal_register)
        val buttonCamera : Button = view.findViewById(R.id.button_camera_register)

        val transaction = parentFragmentManager.beginTransaction()
        parentFragmentManager.saveFragmentInstanceState(this)

         //今のFragmentをスタックしておくことで遷移先から戻るボタンでもどれるようにする
        transaction.addToBackStack("PageRegisterSelectFragment")

        buttonManual.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_page_register_select_to_page_insert_fragment)

//            val insertFragment = PageInsertFragment()
//            // R.id.FragmentContainerに入っているFragmentを取り除いて、新しく別のFragmentを入れる //
//            transaction.replace(R.id.nav_host_fragment, insertFragment)
//            transaction.commit()
        }

        buttonCamera.setOnClickListener {
            this.context?.packageManager?.let {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(it)?.let {
                    if (checkCameraPermission()) {
                        cameraTransition()
                    } else {
                        grantCameraPermission()
                    }
                }
            }


        }

        // TODO: Use the ViewModel
    }
    private fun checkCameraPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.CAMERA) }
    }

    private fun grantCameraPermission() = activity?.let {
        requestPermissions( arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraTransition()
            }
        }
    }
    private  fun cameraTransition(){
        findNavController().navigate(R.id.action_navigation_page_register_select_to_page_camera_fragment)
//        val transaction = parentFragmentManager.beginTransaction()
//        parentFragmentManager.saveFragmentInstanceState(this)
//        val cameraFragment = PageCameraFragment()
//        transaction.replace(R.id.nav_host_fragment,cameraFragment)
//        transaction.commit()
    }

}
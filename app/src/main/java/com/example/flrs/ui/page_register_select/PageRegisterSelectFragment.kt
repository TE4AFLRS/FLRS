package com.example.flrs.ui.page_register_select

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flrs.R
import com.example.flrs.ui.page_camera.PageCameraFragment
import com.example.flrs.ui.page_insert.PageInsertFragment

class PageRegisterSelectFragment : Fragment(R.layout.fragment_page_register_select) {

    companion object {
        fun newInstance() = PageRegisterSelectFragment()
    }

    private lateinit var viewModel: PageRegisterSelectViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageRegisterSelectViewModel::class.java)
        val textView : TextView = view.findViewById(R.id.text_PageRegisterSelect)

        val buttonManual : Button = view.findViewById(R.id.button_manal_register)
        val buttonCamera : Button = view.findViewById(R.id.button_camera_register)

        val transaction = parentFragmentManager.beginTransaction()
        parentFragmentManager.saveFragmentInstanceState(this)

        // 今のFragmentをスタックしておくことで遷移先から戻るボタンでもどれるようにする //
        transaction.addToBackStack("PageRegisterSelectFragment")

        buttonManual.setOnClickListener {
            val insertFragment = PageInsertFragment()
            // R.id.FragmentContainerに入っているFragmentを取り除いて、新しく別のFragmentを入れる //
            transaction.replace(R.id.nav_host_fragment, insertFragment)
            transaction.commit()
        }

        buttonCamera.setOnClickListener {
            val cameraFragment = PageCameraFragment()
            transaction.replace(R.id.nav_host_fragment,cameraFragment)
            transaction.commit()
        }

        // TODO: Use the ViewModel
    }

}
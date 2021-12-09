package com.example.flrs.ui.page_camera

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.flrs.R
import com.example.flrs.ui.page_insert.PageInsertViewModel

class PageCameraFragment : Fragment(R.layout.fragment_page_camera) {

    companion object {
        fun newInstance() = PageCameraFragment()
    }

    private lateinit var viewModel: PageCameraViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageCameraViewModel::class.java)
        // TODO: Use the ViewModel
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack()
        }
    }
}
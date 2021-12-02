package com.example.flrs.ui.page_camera

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flrs.R

class PageCameraFragment : Fragment() {

    companion object {
        fun newInstance() = PageCameraFragment()
    }

    private lateinit var viewModel: PageCameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page_camera, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageCameraViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
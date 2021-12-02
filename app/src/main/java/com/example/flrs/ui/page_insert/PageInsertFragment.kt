package com.example.flrs.ui.page_insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.flrs.R

class PageInsertFragment : Fragment() {

    companion object {
        fun newInstance() = PageInsertFragment()
    }

    private lateinit var viewModel: PageInsertViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_insert, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageInsertViewModel::class.java)
        // TODO: Use the ViewModel
        childFragmentManager.popBackStack()
    }

}
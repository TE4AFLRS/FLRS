package com.example.flrs.ui.insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.flrs.R

class InsertFragment : Fragment(R.layout.fragment_insert) {

    companion object {
        fun newInstance() = InsertFragment()
    }

    private lateinit var viewModel: InsertViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(InsertViewModel::class.java)
        val textView : TextView = view.findViewById(R.id.text_insert)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it

        })
        // TODO: Use the ViewModel
    }
}
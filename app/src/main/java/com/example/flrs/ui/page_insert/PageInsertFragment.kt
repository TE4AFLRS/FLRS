package com.example.flrs.ui.page_insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableRow
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.flrs.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class PageInsertFragment : Fragment(R.layout.fragment_page_insert) {

    companion object {
        fun newInstance() = PageInsertFragment()
    }

    private lateinit var viewModel: PageInsertViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageInsertViewModel::class.java)
        // TODO: Use the ViewModel
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack()

        }

        val vg = view.findViewById<View>(R.id.ResisterField) as ViewGroup
        val fab:FloatingActionButton = view.findViewById(R.id.insert_fab)

        layoutInflater.inflate(R.layout.page_insert_table_row, vg)
        // 行を追加
        fab.setOnClickListener {
            layoutInflater.inflate(R.layout.page_insert_table_row, vg)
        }




    }
}
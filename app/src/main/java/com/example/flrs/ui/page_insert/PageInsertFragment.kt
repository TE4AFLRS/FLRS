package com.example.flrs.ui.page_insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableRow
import androidx.activity.addCallback
import com.example.flrs.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        val vg = view.findViewById<View>(R.id.insert_table) as ViewGroup
        val fab: FloatingActionButton = view.findViewById(R.id.insert_fab)
        layoutInflater.inflate(R.layout.page_insert_table_row, vg)
        // 行を追加
        fab.setOnClickListener {
            layoutInflater.inflate(R.layout.page_insert_table_row, vg)
//            var button = ImageButton(context)
//            var id: Int = View.generateViewId()
//            button.id = id
//            print(button.id)

            val ll = vg.getChildAt(1) as LinearLayout
            (ll.getChildAt(0)as ImageButton)?.setOnClickListener{
                vg.removeViewAt(1)
            }
        }

    }
}
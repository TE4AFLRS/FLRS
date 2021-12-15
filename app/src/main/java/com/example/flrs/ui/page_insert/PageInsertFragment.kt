package com.example.flrs.ui.page_insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.addCallback
import com.example.flrs.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PageInsertFragment : Fragment(R.layout.fragment_page_insert) {

    companion object {
        fun newInstance() = PageInsertFragment()
    }

    private lateinit var viewModel: PageInsertViewModel
    lateinit var mCategoriesDao: CategoriesDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageInsertViewModel::class.java)
        // TODO: Use the ViewModel
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack()
        }
        mCategoriesDao =FoodsDatabase.getInstance(requireContext()).categoriesDao()
        var list:List<CategoryRow> = mCategoriesDao.getAll()
//        var nameList:List<String>
//        Log.d("DEBUG",list.toString())
//        for (i in list){
//            nameList = list.get((i),(1))
//        }

        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,list.map{it.category_name})




        val vg = view.findViewById<View>(R.id.insert_table) as ViewGroup
        val fab: FloatingActionButton = view.findViewById(R.id.insert_fab)
        layoutInflater.inflate(R.layout.page_insert_table_row, vg)
        val spinner:Spinner = view.findViewById(R.id.spinner)
        spinner.adapter = adapter
        // 行を追加
        fab.setOnClickListener {
            val table = layoutInflater.inflate(R.layout.page_insert_table_row, vg) as LinearLayout
            val row = (table.getChildAt(table.childCount - 1) as LinearLayout)
            val spinner:Spinner = row.findViewById(R.id.spinner)
            spinner.adapter = adapter
            (row.getChildAt(0)  as ImageButton)?.setOnClickListener{
                vg.removeView(row)
            }

        }

    }
}
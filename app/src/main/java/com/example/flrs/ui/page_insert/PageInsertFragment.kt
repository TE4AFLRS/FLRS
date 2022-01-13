package com.example.flrs.ui.page_insert

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.room.Insert
import com.example.flrs.*
import com.example.flrs.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class PageInsertFragment : Fragment(R.layout.fragment_page_insert) {

    companion object {
        fun newInstance() = PageInsertFragment()
    }

    private lateinit var viewModel: PageInsertViewModel
    lateinit var mCategoriesDao: CategoriesDao
    lateinit var mFoodsDao: FoodsDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PageInsertViewModel::class.java)
        // TODO: Use the ViewModel

        requireActivity().onBackPressedDispatcher.addCallback(this) {
           findNavController().navigate(R.id.action_page_insert_fragment_to_navigation_page_register_select)
        }

        mCategoriesDao =FoodsDatabase.getInstance(requireContext()).categoriesDao()
        mFoodsDao = FoodsDatabase.getInstance(requireContext()).foodsDao()
        var list:List<CategoryRow> = mCategoriesDao.getAll()
        val adapter = ArrayAdapter(requireContext(),R.layout.spinner_item,list.map{it.category_name})

        val saveButton = view.findViewById<Button>(R.id.save_button)
        val vg = view.findViewById<View>(R.id.insert_table) as ViewGroup
        val fab: FloatingActionButton = view.findViewById(R.id.insert_fab)

        addNewRow(vg, adapter)

        // 行を追加
        fab.setOnClickListener {
            addNewRow(vg, adapter)
        }

        saveButton.setOnClickListener {
            for(i in vg.children){
                val name = i.findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
                val id = i.findViewById<Spinner>(R.id.spinner).selectedItemPosition
                insertFoods(name, list[id].category_id)
            }

//            val transaction = parentFragmentManager.beginTransaction()
//            parentFragmentManager.saveFragmentInstanceState(this)
//            val homeFragment = HomeFragment()
//            // R.id.FragmentContainerに入っているFragmentを取り除いて、新しく別のFragmentを入れる //
//            transaction.replace(R.id.nav_host_fragment, homeFragment)
//            transaction.commit()
            findNavController().navigate(R.id.action_page_insert_fragment_to_navigation_home)
        }
    }

    private fun addNewRow(vg: ViewGroup, adapter: ArrayAdapter<String>) {
        val table = layoutInflater.inflate(R.layout.page_insert_table_row, vg) as LinearLayout
        val row = (table.getChildAt(table.childCount - 1) as LinearLayout)
        val spinner:Spinner = row.findViewById(R.id.spinner)
        spinner.adapter = adapter
        (row.getChildAt(0)  as ImageButton)?.setOnClickListener{
            if (table.childCount > 1) {
                vg.removeView(row)
            }
        }
    }

    @Insert
    private fun insertFoods(foodName:String,categoryId:Int){
        val newFood = Foods(0,foodName,categoryId,LocalDate.now().toString())
        mFoodsDao.insert(newFood)
    }

}
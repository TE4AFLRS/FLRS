package com.example.flrs.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Delete
import com.example.flrs.FoodsDao
import com.example.flrs.FoodsDatabase
import com.example.flrs.R
import com.example.flrs.RowModel
import kotlin.math.log

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var homeViewModel: HomeViewModel

    var db_list = mutableListOf<RowModel>()
    lateinit var mFoodsDao: FoodsDao
    var backGroundColor = R.color.almond

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        //view.setBackgroundResource(backGroundColor)

        mFoodsDao = FoodsDatabase.getInstance(requireContext()).foodsDao()
        db_list = mFoodsDao.getAll().toMutableList()


        db_list
        val rv = view.findViewById<RecyclerView>(R.id.food_recyclerview)
        val adapter = HomeViewAdapter(db_list)

        rv.setHasFixedSize(true)
        rv.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)

        rv.adapter = adapter
        val swipeToDismissTouchHelper = getSwipeToDismissTouchHelper(adapter)
        swipeToDismissTouchHelper.attachToRecyclerView(rv)

        //下線
        val dividerItemDecoration =
            DividerItemDecoration(context, LinearLayoutManager(context).getOrientation())
        rv.addItemDecoration(dividerItemDecoration)


    }

    @Delete
    private fun deleteFoods(position: Int) {
        if (db_list.isEmpty()) return
        val deleteFoods: Int = db_list.get(position).food_id.toInt()
        mFoodsDao.delete(deleteFoods)
    }


    //カードのスワイプアクションの定義
    private fun getSwipeToDismissTouchHelper(adapter: RecyclerView.Adapter<HomeViewHolder>) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //スワイプ時に実行
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //データリストからスワイプしたデータを削除
                deleteFoods(viewHolder.bindingAdapterPosition)
                db_list.removeAt(viewHolder.adapterPosition)

                System.out.println("このIDを消したよ" + viewHolder.itemId)
                //リストからスワイプしたカードを削除
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            //スワイプした時の背景を設定
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val background = ColorDrawable()
                background.color = Color.parseColor("#f44336")

                val deleteIcon = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_delete
                )
                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight)/3
                val iconWidth = (viewHolder.itemView.width-(deleteIcon!!.intrinsicWidth*4))
                if (dX < 0) {
                    deleteIcon.setBounds(
                        itemView.left+iconMarginVertical+iconWidth,
                        itemView.top +iconMarginVertical,
                        itemView.right-iconMarginVertical,
                        itemView.bottom-iconMarginVertical
                    )
                    background.setBounds(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }
                background.draw(c)
                deleteIcon.draw(c)
            }
        })
}
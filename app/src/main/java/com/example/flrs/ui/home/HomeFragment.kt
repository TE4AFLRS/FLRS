package com.example.flrs.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flrs.R
import com.example.flrs.RowModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var homeViewModel: HomeViewModel

    var db_List = mutableListOf<RowModel>()
    lateinit var mFoodsDao: FoodsDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        mFoodsDao = FoodsDatabase.getInstance(this).foodsDao()
        db_list = mFoodsDao.getAll().toMutableList()
        val rv = view.findViewById<RecyclerView>(R.id.food_recyclerview)
        val adapter = ViewAdapter(db_list, object : ViewAdapter.ListListener {})

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

        //画面戻る処理
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.popBackStack()
        }

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
                    db_List.removeAt(viewHolder.adapterPosition)
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
                    if (dX < 0)
                        background.setBounds(
                                itemView.right + dX.toInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                        )
                    else
                        background.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.left + dX.toInt(),
                                itemView.bottom
                        )
                    background.draw(c)
                }
            })
}
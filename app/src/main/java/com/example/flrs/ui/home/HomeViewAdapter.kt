package com.example.flrs.ui.home
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flrs.R
import com.example.flrs.RowModel
import java.time.LocalDate


class HomeViewAdapter(private val list: List<RowModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        Log.d("Life Cycle", "onCreateViewHolder")
        val rowView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.food_recyclerview_row, parent, false)
        return HomeViewHolder(rowView)
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        Log.d("Life Cycle", "onBindViewHolder")
        holder.foodNameView.text=list[position].food_name

        var date : LocalDate = LocalDate.parse(list[position].register_date)
        holder.foodRegisterDateView.text = list[position].register_date
        holder.foodLimitDateView.text = date.plusDays(30).toString()
    }
    override fun getItemCount(): Int {
        Log.d("Life Cycle", "getItemCount")
        return list.size
    }
}

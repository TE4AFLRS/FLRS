package com.example.flrs.ui.home

import android.view.View
import android.widget.TextView
import com.example.flrs.R

class HomeViewHolder(itemView: View) :
androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    val foodNameView:TextView= itemView.findViewById(R.id.food_name)
    val foodRegisterDateView:TextView = itemView.findViewById(R.id.food_register_date)
    val foodLimitDateView:TextView = itemView.findViewById(R.id.food_limit_date)
}
package com.example.expenseease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Income

class IncomeGroupRecyclerAdapter(private var dataset: List<Pair<String, List<Income>>>): RecyclerView.Adapter<IncomeGroupRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView
        val incomes: RecyclerView
        init{
            date = view.findViewById(R.id.date)
            incomes = view.findViewById(R.id.recyclerView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_expense_income_item, parent, false)

        return IncomeGroupRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = dataset[position]
        holder.date.text = dataset[position].first
        val adapter = IncomeRecyclerAdapter(dataset[position].second)
        holder.incomes.adapter = adapter
        holder.incomes.layoutManager = LinearLayoutManager(holder.date.context)

    }

}
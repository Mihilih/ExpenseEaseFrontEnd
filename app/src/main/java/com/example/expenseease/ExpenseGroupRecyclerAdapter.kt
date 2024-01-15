package com.example.expenseease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Expense

class ExpenseGroupRecyclerAdapter(private var dataset: List<Pair<String, List<Expense>>>): RecyclerView.Adapter<ExpenseGroupRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView
        val expenses: RecyclerView
        init{
            date = view.findViewById(R.id.date)
            expenses = view.findViewById(R.id.recyclerView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_expense_income_item, parent, false)

        return ExpenseGroupRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = dataset[position]
        holder.date.text = dataset[position].first.substringBefore(" ")
        val adapter = ExpenseRecyclerAdapter(dataset[position].second)
        holder.expenses.adapter = adapter
        holder.expenses.layoutManager = LinearLayoutManager(holder.date.context)

    }

}
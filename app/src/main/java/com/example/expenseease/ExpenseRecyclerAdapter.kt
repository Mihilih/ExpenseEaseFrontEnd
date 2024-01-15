package com.example.expenseease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Expense

class ExpenseRecyclerAdapter(private var dataset: List<Expense>): RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        val amount: TextView
        val description: TextView
        val category: TextView
        init{
            name = view.findViewById(R.id.name)
            amount = view.findViewById(R.id.amount)
            description = view.findViewById(R.id.description)
            category = view.findViewById(R.id.category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_income_item, parent, false)

        return ExpenseRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ExpenseRecyclerAdapter.ViewHolder, position: Int) {
        val expense  = dataset[position]
        holder.name.text = expense.name
        holder.description.text = expense.description
        holder.amount.text = expense.amount.toString()
        holder.category.text = expense.category.toString()
        //get category name instead but this is fine for now

    }

}
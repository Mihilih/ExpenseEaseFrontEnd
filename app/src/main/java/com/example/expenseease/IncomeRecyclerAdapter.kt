package com.example.expenseease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Expense
import com.example.expenseease.data.Income

class IncomeRecyclerAdapter(private var dataset: List<Income>): RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder>()  {

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

        return IncomeRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: IncomeRecyclerAdapter.ViewHolder, position: Int) {


        val instance = SessionRepository(context =holder.name.context)
        val income  = dataset[position]
        holder.name.text = income.name
        holder.description.text = income.description
        holder.amount.text = "$${income.amount.toString()}"
        holder.category.text = income.category.toString()
        holder.category.text = instance?.getCategories()?.filter { it.id==income.category }?.first()?.name.toString()
        //get category name instead but this is fine for now

    }

}
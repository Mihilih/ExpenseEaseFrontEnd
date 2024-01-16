package com.example.expenseease

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.ConfirmActivity
import com.example.expenseease.data.Expense

class ExpenseRecyclerAdapter(private var dataset: List<Expense>): RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder>()  {



    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        val amount: TextView
        val description: TextView
        val category: TextView
        val card: CardView
        init{
            name = view.findViewById(R.id.name)
            amount = view.findViewById(R.id.amount)
            description = view.findViewById(R.id.description)
            category = view.findViewById(R.id.category)
            card = view.findViewById(R.id.card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_income_item, parent, false)

        return ExpenseRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ExpenseRecyclerAdapter.ViewHolder, position: Int) {

        val instance = SessionRepository(context =holder.name.context)

        val expense  = dataset[position]
        holder.name.text = expense.name
        holder.description.text = expense.description
        holder.amount.text = "$${expense.amount.toString()}"
        holder.category.text = instance?.getCategories()?.filter { it.id==expense.category }?.first()?.name.toString()
        holder.card.setOnClickListener{
            val intent =  Intent(holder.card.context, IndividualIncomeExpense::class.java)
            intent.putExtra("name", expense.name)
            intent.putExtra("description", expense.description)
            intent.putExtra("category", expense.category)
            intent.putExtra("amount", expense.amount)
            intent.putExtra("id", expense.id)
            intent.putExtra("date", expense.date)
            intent.putExtra("isexpense", false)
            holder.card.context.startActivity(intent)
        }

    }

}
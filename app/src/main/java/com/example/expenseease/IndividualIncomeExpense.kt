package com.example.expenseease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class IndividualIncomeExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_income_expense)
        val name = intent.extras?.getString("name")
        val description = intent.extras?.getString("description")
        val amount = intent.extras?.getInt("amount")
        val category = intent.extras?.getInt("category")
        val date = intent.extras?.getString("date")

        val id = intent.extras?.getInt("id")
        val isexpense = intent.extras?.getBoolean("isexpense")

        val instance = SessionRepository(context = this)

        val nameText:TextView = findViewById(R.id.nameText)
        nameText.text = name
        val descriptionText:TextView = findViewById(R.id.descriptionText)
        descriptionText.text = description
        val amountText:TextView = findViewById(R.id.amountText)
        amountText.text = amount.toString()
        val dateText:TextView = findViewById(R.id.dateText)
        dateText.text = "on ${date?.substringBefore(" ")}"
        val categoryText: TextView = findViewById(R.id.categoryText)
        categoryText.text = instance?.getCategories()?.filter { it.id==category }?.first()?.name.toString()
    }
}
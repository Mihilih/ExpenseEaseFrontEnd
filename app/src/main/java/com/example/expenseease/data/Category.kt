package com.example.expenseease.data

data class Category(
    var id: Int,
    var name: String,
    var expenses: List<Expense>,
    var income: List<Income>
    )

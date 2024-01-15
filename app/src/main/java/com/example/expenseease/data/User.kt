package com.example.expenseease.data

data class User(
    var id: Int,
    var first_name: String,
    var last_name: String,
    var email: String,
    var current_balance: Int,
    var categories: List<Category>,
    var expenses: List<Expense>,
    var incomes: List<Income>,
)

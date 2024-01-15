package com.example.expenseease.data

data class Expense(
    var id: Int,
    var name: String,
    var description: String,
    var amount: Int,
    var user: Int,
    var category: Int,
    var date: String,
)

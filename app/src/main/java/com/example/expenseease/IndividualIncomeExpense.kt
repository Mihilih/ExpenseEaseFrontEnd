package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

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


        val editButton: Button = findViewById(R.id.editButton)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        editButton.setOnClickListener{
            val intent = Intent(this, EditIncomeExpense::class.java)
            intent.putExtra("name", name)
            intent.putExtra("description", description)
            intent.putExtra("amount", amount)
            intent.putExtra("category", category)
            intent.putExtra("id", id)
            intent.putExtra("isexpense", isexpense)

            startActivity(intent)
        }
        deleteButton.setOnClickListener{
            val client = OkHttpClient()
            var url1 = "http://34.29.154.243/api/expense/${id}"
            if (!isexpense!!){
                url1="http://34.29.154.243/api/income/${id}"
            }
            val request = Request.Builder()
                .url(url1)
                .header("Authorization", "Bearer "+ (instance?.getSessionToken() ?:""))
                .delete()
                .build()
            val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("LOGLOGLOG", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()

                    if (res != null) {
                        Log.e("LOGLOGLOG", res)
                        Log.e("LOGLOGLOG", instance?.getSessionToken() ?:"")
                        if (!res.contains("error")){
                            instance?.updateUser()
                            val intent = Intent(this@IndividualIncomeExpense, MainActivity::class.java)
                            startActivity(intent)
                        }else if (res.contains("Authorization") or res.contains("session")){
                            instance?.updateUser()
                            val intent = Intent(this@IndividualIncomeExpense, OnboardingActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }
            })

        }

    }
}
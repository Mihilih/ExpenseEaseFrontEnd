package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class EditIncomeExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_income_expense)
        val name: EditText = findViewById(R.id.nameText)
        val description: EditText = findViewById(R.id.descriptionText)
        val amount: EditText = findViewById(R.id.amountText)
        val instance = SessionRepository(context = this)
        var curr_cat: Int? = intent.extras?.getInt("category")
        val categories = instance?.getCategories()
        val items1 = arrayListOf<String>()

        name.setText(intent.extras?.getString("name"))
        description.setText(intent.extras?.getString("description"))
        amount.setText(intent.extras?.getInt("amount").toString())

        val id = intent.extras?.getInt("id")
        val isexpense = intent.extras?.getBoolean("isexpense")

        val indexList = arrayListOf<Int>()
        if (categories != null) {
            for (cat in categories){
                items1.add(cat.name)
                indexList.add(cat.id)
            }
            runOnUiThread{
                val autoComplete: AutoCompleteTextView = findViewById(R.id.autoCompleteCategory)
                val adapter1 =ArrayAdapter(this, R.layout.list_item, items1)
                autoComplete.setAdapter(adapter1)


                autoComplete.listSelection = 2
                //autoComplete.setText(items1[curr_cat!!])
                autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                        adapterView, view, i, l ->
                    val itemSelected = adapterView.getItemAtPosition(i)
                    curr_cat = indexList[i]
                }
            }

        }

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener{

            val client = OkHttpClient()
            var url1 = "http://34.29.154.243/api/expense/${id}"
            if (!isexpense!!){
                url1="http://34.29.154.243/api/income/${id}"
            }

            val user = instance?.getUser()
            val body: JSONObject = JSONObject()
            body.put("name", name.text.toString())
            body.put("description", description.text.toString())
            if (user != null) {
                body.put("user", user.id)
            }
            body.put("category", curr_cat)
            body.put("amount", amount.text.toString().toInt())
            val request = Request.Builder()
                .url(url1)
                .header("Authorization", "Bearer "+ (instance?.getSessionToken() ?:""))
                .post(body.toString().toRequestBody(RegisterActivity.MEDIA_TYPE_JSON))
                .build()
            val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Log", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()

                    if (res != null) {
                        if (!res.contains("error")){
                            instance?.updateUser()
                            val intent = Intent(this@EditIncomeExpense, MainActivity::class.java)
                            startActivity(intent)
                        }else if (res.contains("Authorization") or res.contains("session")){
                            instance?.updateUser()
                            val intent = Intent(this@EditIncomeExpense, OnboardingActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }
            })

        }

    }
}
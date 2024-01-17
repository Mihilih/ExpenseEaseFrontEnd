package com.example.expenseease.data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.expenseease.MainActivity
import com.example.expenseease.OnboardingActivity
import com.example.expenseease.R
import com.example.expenseease.SessionRepository
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val no: Button = findViewById(R.id.noButton)
        val yes: Button = findViewById(R.id.yesButton)
        no.setOnClickListener{
            val intent = Intent(this@ConfirmActivity, MainActivity::class.java)
            startActivity(intent)
        }
        yes.setOnClickListener{
            val client = OkHttpClient()
            val url = "http://34.29.154.243/api/users"
            val instance = SessionRepository(context = this)

            val request = Request.Builder()
                .url(url)
                .delete()
                .header("Authorization", "Bearer "+ (instance?.getSessionToken() ?:""))
                .build()
            val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()

                    if (res != null) {
                    }
                    if (instance != null) {
                        instance.deleteData()
                    }
                    val intent =  Intent(applicationContext, OnboardingActivity::class.java)
                    startActivity(intent)

                }
            })
        }
    }
}
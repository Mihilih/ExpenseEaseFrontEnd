package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val saveButton: Button = findViewById(R.id.saveButton)
        val email: TextInputEditText = findViewById(R.id.uname)
        val pw: TextInputEditText = findViewById(R.id.pw)


        saveButton.setOnClickListener{
            val client = OkHttpClient()
            val url = "http://34.29.154.243/api/login/"
            val body: JSONObject = JSONObject()
            body.put("email", email.text.toString())
            body.put("password", pw.text.toString())
            Log.e("LOGLOGLOG", body.toString())
            val request = Request.Builder()
                .url(url)
                .post(body.toString().toRequestBody(RegisterActivity.MEDIA_TYPE_JSON))
                .build()

            val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread{
                        Log.e("LOGLOGLOG", e.toString())
                        Toast.makeText(this@LoginActivity, "Failed to login user", Toast.LENGTH_SHORT).show()

                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()

                    if (res != null) {
                        Log.e("RESPONSE", res)
                    }
                    runOnUiThread{
                        if (res != null) {
                            if (res.contains("session_token")){
                                //parse and store session details
                                val instance = SessionRepository(context = this@LoginActivity)
                                instance.storeData(res)
                                Log.e("LOGLOGLOG", instance.getUpdateToken())

                                instance.updateUser()




                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@LoginActivity, "Failed to login user", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@LoginActivity, "Failed to login user", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            })

        }

    }
}
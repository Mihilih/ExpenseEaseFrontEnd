package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val saveButton: Button = findViewById(R.id.saveButton)
        val fname: TextInputEditText = findViewById(R.id.fname)
        val lname: TextInputEditText = findViewById(R.id.lname)
        val uname: TextInputEditText = findViewById(R.id.uname)
        val pw: TextInputEditText = findViewById(R.id.pw)
        val email: TextInputEditText = findViewById(R.id.email)
        val amt: TextInputEditText = findViewById(R.id.amt)

        val fnameText = intent.extras?.getString("first_name")
        val lnameText = intent.extras?.getString("last_name")
        val unameText = intent.extras?.getString("username")
        val emailText = intent.extras?.getString("email")
        val amountText = intent.extras?.getInt("amount")
        val id = intent.extras?.getInt("id")

        if (fnameText!=null){

            fname.setText(fnameText)
            lname.setText(lnameText)
            uname.setText(unameText)
            pw.setText("password cannot be changed")
            email.setText(emailText)
            amt.setText(amountText.toString())
        }

        saveButton.setOnClickListener{
            if (fnameText==null){
                val client = OkHttpClient()
                val url = "http://34.29.154.243/api/users/"
                val body: JSONObject = JSONObject()
                body.put("first_name", fname.text.toString())
                body.put("last_name", lname.text.toString())
                body.put("username", uname.text.toString())
                body.put("email", email.text.toString())
                body.put("password", pw.text.toString())
                body.put("amt", amt.text.toString().toInt())
                Log.e("LOGLOGLOG", body.toString())
                val request = Request.Builder()
                    .url(url)
                    .post(body.toString().toRequestBody(MEDIA_TYPE_JSON))
                    .build()

                val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread{
                            Log.e("LOGLOGLOG", e.toString())
                            Toast.makeText(this@RegisterActivity, "Failed to create user", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val res = response.body?.string()

                        if (res != null) {
                            Log.e("RESPONSE", res)
                        }
                        runOnUiThread{
                            // {"session_token": "8fb021795e216a95556817cd12766066f4a4e986", "session_expiration": "2024-01-14 22:19:26.980979", "update_token": "ba7c5121d08117389d4d37801be658d3f0decaa4"}
                            if (res != null) {
                                if (res.contains("session_token")){
                                    //parse and store session details
                                    val instance = SessionRepository(context = this@RegisterActivity)
                                    instance.storeData(res)
                                    instance.updateUser()
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this@RegisterActivity, "Failed to create user", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@RegisterActivity, "Failed to create user", Toast.LENGTH_SHORT).show()
                            }
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                        }

                    }
                })
            }else{

                val body: JSONObject = JSONObject()
                body.put("first_name", fname.text.toString())
                body.put("last_name", lname.text.toString())
                body.put("username", uname.text.toString())
                body.put("email", email.text.toString())
                body.put("amt", amt.text.toString().toInt())
                val instance = SessionRepository(context = this)

                val client = OkHttpClient()
                val url = "http://34.29.154.243/api/users/${id}"
                Log.e("LOGLOGLOG", body.toString())
                val request = Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer "+ (instance?.getSessionToken() ?:""))
                    .post(body.toString().toRequestBody(MEDIA_TYPE_JSON))
                    .build()

                val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread{
                            Log.e("LOGLOGLOG", e.toString())
                            Toast.makeText(this@RegisterActivity, "Failed to create user", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val res = response.body?.string()

                        if (res != null) {
                            Log.e("RESPONSE", res)
                        }
                        runOnUiThread{
                            // {"session_token": "8fb021795e216a95556817cd12766066f4a4e986", "session_expiration": "2024-01-14 22:19:26.980979", "update_token": "ba7c5121d08117389d4d37801be658d3f0decaa4"}
                            if (res != null) {
                                if (res.contains("first_name")){
                                    //parse and store session details
                                    val instance = SessionRepository(context = this@RegisterActivity)
                                    instance.updateUser()
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this@RegisterActivity, "Failed to update user", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@RegisterActivity, "Failed to update user", Toast.LENGTH_SHORT).show()
                            }
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                        }

                    }
                })
            }
        }

    }

    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}
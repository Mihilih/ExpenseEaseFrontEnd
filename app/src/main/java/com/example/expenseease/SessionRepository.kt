package com.example.expenseease


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.expenseease.data.Category
import com.example.expenseease.data.SessionData
import com.example.expenseease.data.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class SessionRepository@Inject constructor(val context: Context){
    val sharedPreference: SharedPreferences = context.getSharedPreferences("CURRENT_SESSION", Context.MODE_PRIVATE)

    fun getCategories(): List<Category>?{
        val res: String = sharedPreference.getString("categories", "") ?: ""
        return parseCategory(res)
    }

    fun storeCategories(res: String) {
        val editor = sharedPreference.edit()
        editor.putString("categories", res)
        editor.apply()
    }

    fun updateCategories(){
        val client = OkHttpClient()
        val url = "http://34.29.154.243/api/categories/"
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).enqueue(object :okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()

                if (res != null) {
                    storeCategories(res)
                }


            }
        })
    }

    fun updateUser(){
        val client = OkHttpClient()
        val url = "http://34.29.154.243/"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer "+ (getSessionToken() ?:""))
            .build()
        val response = client.newCall(request).enqueue(object :okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                val intent = Intent(context, OnboardingActivity::class.java)
                context.startActivity(intent)
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()

                if (res != null) {
                    storeUser(res)
                }
            }
        })
    }

    fun storeUser(res: String){
        val editor = sharedPreference.edit()
        editor.putString("user", res)
        editor.apply()
    }

    fun getUser(): User? {
        val res: String = sharedPreference.getString("user", "") ?: ""
        return parsePerson(res)
    }

    fun storeData(res: String){
        val editor = sharedPreference.edit()
        val data = parseSession(res)
        editor.putString("session_token", data?.session_token)
        editor.putString("update_token", data?.update_token)
        editor.putString("session_expiration", data?.session_expiration)
        editor.apply()




    }

    fun getSessionToken():String{
        return sharedPreference.getString("session_token", "")?:""
    }

    fun getUpdateToken():String{
        return sharedPreference.getString("update_token", "")?:""
    }

    fun deleteData(){
        val editor = sharedPreference.edit()
        editor.putString("session_token", "")
        editor.putString("update_token", "")
        editor.putString("session_expiration", "")
        editor.apply()
    }
}



private fun parseSession(res : String?): SessionData? {
    try{
        val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<SessionData> = moshi.adapter(SessionData::class.java)
        val parsedSession =  jsonAdapter.fromJson(res)
        return parsedSession
    }catch (x:Exception){
        Log.e("Error", x.toString())
        return null
    }
}

private fun parsePerson(res : String?): User? {
    try{
        val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        val parsedUser =  jsonAdapter.fromJson(res)
        return parsedUser
    }catch (x:Exception){
        Log.e("error", x.toString())
        return null
    }
}

private fun parseCategory(res : String?): List<Category>? {
    try{
        val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
        val categoryListType = Types.newParameterizedType(List::class.java, Category::class.java)
        val jsonAdapter: JsonAdapter<List<Category>> = moshi.adapter(categoryListType)
        val parsedUser =  jsonAdapter.fromJson(res)
        return parsedUser
    }catch (x:Exception){
        Log.e("Log", x.toString())
        return null
    }
}

package com.example.expenseease

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.expenseease.data.Category
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewExpenseFragment() : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.layout_dialog_addnew, container, false)
        val name : TextInputEditText = view.findViewById(R.id.name)
        val description : TextInputEditText = view.findViewById(R.id.description)
        val amount : TextInputEditText = view.findViewById(R.id.amount)
        val category : TextInputLayout = view.findViewById(R.id.dropDown2)

        val instance = activity?.applicationContext?.let { SessionRepository(context = it) }
        var cat: Int? = null
        var isexpense: Boolean = true
        instance?.updateCategories()
        val categories = instance?.getCategories()
        val items1 = arrayListOf<String>()

        val indexList = arrayListOf<Int>()
        if (categories != null) {
            for (cat in categories){
                items1.add(cat.name)
                indexList.add(cat.id)
            }
            getActivity()?.runOnUiThread{
                val autoComplete: AutoCompleteTextView = view.findViewById(R.id.autoCompleteCategory)
                val adapter1 =
                    getActivity()?.let { ArrayAdapter(it.applicationContext, R.layout.list_item, items1) }
                autoComplete.setAdapter(adapter1)
                autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                        adapterView, view, i, l ->
                    val itemSelected = adapterView.getItemAtPosition(i)
                    cat = indexList[i]
                }
            }

        }


        val dropDown: TextInputLayout = view.findViewById(R.id.dropDown)
        val items = listOf("Income", "Expense")
        val autoComplete: AutoCompleteTextView = view.findViewById(R.id.autoComplete)
        val adapter =
            getActivity()?.let { ArrayAdapter(it.applicationContext, R.layout.list_item, items) }
        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i)
            isexpense = itemSelected != "Income"
        }

        val saveButton: Button = view.findViewById(R.id.saveButton)
        val closeButton: Button = view.findViewById(R.id.cancelButton)
        closeButton.setOnClickListener {
            dismiss()
        }
        saveButton.setOnClickListener {
            try{
                val check = amount.text.toString().toInt()
                val user = instance?.getUser()
                val client = OkHttpClient()
                var url1 = "http://34.29.154.243/api/expense/"
                if (!isexpense){
                    url1="http://34.29.154.243/api/income/"
                }
                val body: JSONObject = JSONObject()
                body.put("name", name.text.toString())
                if (user != null) {
                    body.put("user", user.id)
                }
                body.put("description", description.text.toString())
                body.put("category", cat)
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
                                val intent = Intent(activity?.applicationContext, MainActivity::class.java)
                                startActivity(intent)
                            }else if (res.contains("Authorization") or res.contains("session")){
                                instance?.updateUser()
                                val intent = Intent(activity?.applicationContext, OnboardingActivity::class.java)
                                startActivity(intent)
                            }
                        }

                    }
                })
            }catch (x:Exception){
                activity?.runOnUiThread{
                    Toast.makeText(activity?.applicationContext, "Enter valid amount", Toast.LENGTH_SHORT).show()
                }
            }



        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment layout_dialog_addnew.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewExpenseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
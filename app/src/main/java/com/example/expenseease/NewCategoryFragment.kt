package com.example.expenseease

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
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
 * Use the [NewCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewCategoryFragment : DialogFragment() {
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
        val view = inflater.inflate(R.layout.fragment_new_category, container, false)
        val name: TextInputEditText = view.findViewById(R.id.name)
        val saveButton: Button = view.findViewById(R.id.saveButton)
        val closeButton: Button = view.findViewById(R.id.cancelButton)
        val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }

        closeButton.setOnClickListener {
            dismiss()
        }

        saveButton.setOnClickListener{
            val client = OkHttpClient()
            var url = "http://34.29.154.243/api/categories/"
            val body: JSONObject = JSONObject()
            body.put("name", name.text.toString())
            body.put("isexpense", true)
            val request = Request.Builder()
                .url(url)
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
                            name.setText("")
                            instance?.updateCategories()
                            activity?.runOnUiThread{
                                Toast.makeText(activity?.applicationContext, "Created", Toast.LENGTH_SHORT).show()
                            }

                        }else if (res.contains("Authorization") or res.contains("session")){
                            val intent = Intent(activity?.applicationContext, OnboardingActivity::class.java)
                            startActivity(intent)
                        }else{
                            activity?.runOnUiThread{
                                Toast.makeText(activity?.applicationContext, "Category Already Exists", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            })



        }

        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
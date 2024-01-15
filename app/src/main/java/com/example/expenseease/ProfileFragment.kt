package com.example.expenseease

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.expenseease.data.User
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
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        val name:TextView = view.findViewById(R.id.nameText)
        val username:TextView = view.findViewById(R.id.usernameText)
        val amt:TextView = view.findViewById(R.id.currDataText)
        val editButton: Button = view.findViewById(R.id.editButton)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)


        val instance = activity?.let { SessionRepository(context = it.applicationContext) }
        val user = instance?.getUser()
        name.text = "${user?.first_name} ${user?.last_name}"
        username.text = "@${user?.email}"
        amt.text = "$${user?.current_balance}!!!"

        logoutButton.setOnClickListener(){
            val client = OkHttpClient()
            val url = "http://34.29.154.243/api/logout/"
            val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer"+ (instance?.getSessionToken() ?:""))
                .build()
            val response = client.newCall(request).enqueue(object :okhttp3.Callback{
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()

                    if (res != null) {
                        Log.e("LOGLOGLOG", res)
                    }
                    if (instance != null) {
                        instance.deleteData()
                    }
                    val intent =  getActivity()?.let { it1 ->Intent(it1.getApplicationContext(), OnboardingActivity::class.java)}
                    startActivity(intent)

                }
            })
        }


        return view
    }

    private fun parsePerson(res : String?): User? {
        try{
            val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
            val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
            val parsedUser =  jsonAdapter.fromJson(res)
            Log.e("LOGLOGLOG", "success")
            Log.e("LOGLOGLOG", parsedUser.toString())
            return parsedUser
        }catch (x:Exception){
            Log.e("error", x.toString())
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
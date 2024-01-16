package com.example.expenseease

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Expense
import com.example.expenseease.data.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_expense, container, false)
        val recycler : RecyclerView = view.findViewById(R.id.expenseRecyclerView)
        var expenses = listOf<Expense>()
        val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }

        val user = instance?.getUser()
        if (user != null) {
            expenses = user.expenses
            Log.e("LOGLOGLOG", expenses.toString())
        }


        val expenseGroup = arrayListOf<Pair<String, List<Expense>>>()
        var group = arrayListOf<Expense>()
        var currentDate = "0"
        if (!group.isEmpty()){
            currentDate = expenses.first().date
        }
        for (expense in expenses){
            if (expense.date==currentDate){
                group.add(expense)
            }else{
                if (!group.isEmpty()){
                    expenseGroup.add(Pair<String, List<Expense>>(currentDate,group))
                }
                currentDate=expense.date
                group = arrayListOf<Expense>()
                group.add(expense)
            }
        }
        if (!group.isEmpty()) {
            expenseGroup.add(Pair<String, List<Expense>>(currentDate, group))
        }
        val adapter = ExpenseGroupRecyclerAdapter(expenseGroup)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

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
         * @return A new instance of fragment ExpenseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpenseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
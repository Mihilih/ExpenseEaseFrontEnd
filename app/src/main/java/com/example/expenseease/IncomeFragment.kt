package com.example.expenseease

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseease.data.Category
import com.example.expenseease.data.Expense
import com.example.expenseease.data.Income
import com.example.expenseease.data.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
 * Use the [IncomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncomeFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_income, container, false)
        val recycler : RecyclerView = view.findViewById(R.id.incomeRecyclerView)
        var incomes = listOf<Income>()

        val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }

        val user = instance?.getUser()
        if (user != null) {
            incomes = user.incomes
        }

        val incomeGroup = arrayListOf<Pair<String, List<Income>>>()
        var group = arrayListOf<Income>()
        var currentDate = "0"
        if (!group.isEmpty()){
            currentDate = incomes.first().date
        }

        for (income in incomes){
            if (income.date==currentDate){
                group.add(income)
            }else{
                if (!group.isEmpty()){
                    incomeGroup.add(Pair<String, List<Income>>(currentDate,group))
                }
                currentDate=income.date
                group = arrayListOf<Income>()
                group.add(income)
            }
        }


        if (!group.isEmpty()) {
            incomeGroup.add(Pair<String, List<Income>>(currentDate, group))
        }
        val adapter = IncomeGroupRecyclerAdapter(incomeGroup)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        return view
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
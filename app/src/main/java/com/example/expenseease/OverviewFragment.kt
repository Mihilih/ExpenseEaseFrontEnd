package com.example.expenseease

import android.os.Bundle
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Typeface
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.expenseease.data.Expense
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
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
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_overview, container, false)
        val pieChart: PieChart = view.findViewById(R.id.chart)
        val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }
        var expenses = listOf<Expense>()
        instance?.updateUser()
        val user = instance?.getUser()
        if (user != null) {
            expenses = user.expenses
            Log.e("LOGLOGLOG", expenses.toString())
        }


        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)

        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.setHoleRadius(50f)
        pieChart.setTransparentCircleRadius(53f)

        pieChart.setRotationAngle(0f)

        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false


        expenses = expenses.sortedBy { it.category }
        Log.e("LOGLOGLOGE", expenses.toString())
        val expenseGroup = arrayListOf<Pair<String, Int>>()
        var group = arrayListOf<Expense>()
        var sum = 0
        var currentcat = 0
        if (!group.isEmpty()){
            currentcat = expenses.first().category
        }
        for (expense in expenses){
            if (expense.category==currentcat){
                group.add(expense)
                sum = sum + expense.amount
            }else{
                if (!group.isEmpty()){
                    val cat = instance?.getCategories()?.filter { it.id==currentcat }?.first()
                    expenseGroup.add(Pair<String, Int>(cat?.name.toString(),sum))
                }
                currentcat=expense.category
                group = arrayListOf<Expense>()
                sum = expense.amount
                group.add(expense)
            }
        }
        if (!group.isEmpty()) {
            val cat = instance?.getCategories()?.filter { it.id==currentcat }?.first()
            expenseGroup.add(Pair<String, Int>(cat?.name.toString(), sum))
        }
        Log.e("LOGLOGLOG", expenseGroup.toString())


        val entries: ArrayList<PieEntry> = ArrayList()
        for (expense in expenseGroup){
            entries.add(PieEntry(expense.second.toFloat(), expense.first))
        }

        val dataSet = PieDataSet(entries, "Mobile OS")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 7f

        val colors: ArrayList<Int> = ArrayList()

        for (expense in expenses){
            colors.add(resources.getColor(R.color.mint_green))
            colors.add(resources.getColor(R.color.gray))
        }
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        val typeface: Typeface? =
            getActivity()?.let { ResourcesCompat.getFont(it.getApplicationContext(), R.font.montserratitalic) };
        data.setValueTypeface(typeface)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)
        pieChart.setEntryLabelTypeface(typeface)
        pieChart.highlightValues(null)
        pieChart.invalidate()

        val addButton: Button = view.findViewById(R.id.newButton)
        val categoryButton: Button = view.findViewById(R.id.categoryButton)
        val emailButton: Button = view.findViewById(R.id.emailButton)
        addButton.setOnClickListener{

            val newFragment = NewExpenseFragment()
            activity?.let { it1 -> newFragment.show(it1.supportFragmentManager, "") }
        }

        categoryButton.setOnClickListener{
            val newFragment = NewCategoryFragment()
            activity?.let { it1 -> newFragment.show(it1.supportFragmentManager, "") }
        }
        emailButton.setOnClickListener{
            val client = OkHttpClient()
            val url = "http://34.29.154.243/api/email/"
            val instance = getActivity()?.let { it1 -> SessionRepository(context = it1.getApplicationContext()) }

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer "+ (instance?.getSessionToken() ?:""))
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
                    activity?.runOnUiThread{
                        Toast.makeText(activity?.applicationContext, "Sent email report successfully", Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OverviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OverviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
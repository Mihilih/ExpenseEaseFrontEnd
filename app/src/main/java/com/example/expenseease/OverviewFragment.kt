package com.example.expenseease

import android.os.Bundle
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Typeface
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.expenseease.data.Expense
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData

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


        val expenses = arrayListOf(
            Expense(
                id = 1,
                name = "groceries",
                description = "snacks and shit",
                amount = 90,
                date = "2024-04-12",
                user = 1,
                category = 2
            ),
            Expense(
                id = 2,
                name = "books",
                description = "school books",
                amount = 70,
                date = "2024-04-12",
                user = 1,
                category = 2
            ),
            Expense(
                id = 3,
                name = "CUHealth",
                description = "I was sick",
                amount = 50,
                date = "2024-05-12",
                user = 1,
                category = 2
            ),
            Expense(
                id = 4,
                name = "CUHealth",
                description = "I was sick",
                amount = 20,
                date = "2024-05-12",
                user = 1,
                category = 2
            ),
            Expense(
                id = 5,
                name = "CUHealth",
                description = "I was sick",
                amount = 40,
                date = "2024-05-12",
                user= 1,
                category = 2
            ),
            Expense(
                id = 6,
                name = "CUHealth",
                description = "I was sick",
                amount = 100,
                date = "2024-05-12",
                user= 1,
                category = 2
            ),
        )



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

        val entries: ArrayList<PieEntry> = ArrayList()
        for (expense in expenses){
            entries.add(PieEntry(expense.amount.toFloat(), expense.name))
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

        addButton.setOnClickListener{

            val newFragment = NewExpenseFragment()
            activity?.let { it1 -> newFragment.show(it1.supportFragmentManager, "Availabilities") }
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
package com.example.expenseease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavBar: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId){
                R.id.expenseItem ->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ExpenseFragment.newInstance("", "")).commit()
                }
                R.id.incomeItem->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, IncomeFragment.newInstance("", "")).commit()
                }
                R.id.profileItem ->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProfileFragment.newInstance("", "")).commit()
                }
                R.id.overviewItem->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, OverviewFragment.newInstance("", "")).commit()
                }

            }
            true
        }
    }
}
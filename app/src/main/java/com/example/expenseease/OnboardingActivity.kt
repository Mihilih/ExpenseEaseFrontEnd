package com.example.expenseease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val loginButton: Button = findViewById(R.id.button)
        val alternateText: TextView = findViewById(R.id.alternateText)

        alternateText.setOnClickListener{
            if (loginButton.text=="Login"){
                loginButton.text="Register"
                alternateText.text="I already have an account"
            }else{
                loginButton.text="Login"
                alternateText.text="I do not have an account"
            }
        }

        loginButton.setOnClickListener{
            if (loginButton.text=="Login"){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
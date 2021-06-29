package com.alexparra.bankaccountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.alexparra.bankaccountapp.model.Account

class MainActivity : AppCompatActivity() {
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, AccountScreen::class.java)
            startActivity(intent)
        }

    }

    private fun login() {
        TODO()
    }
}
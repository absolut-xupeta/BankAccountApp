package com.alexparra.bankaccountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import com.alexparra.bankaccountapp.csv.AccountsManager
import com.alexparra.bankaccountapp.model.Account

const val LOGGED_USER = "LOGGED_USER"

class MainActivity : AppCompatActivity() {
    lateinit var loginButton: Button
    lateinit var accountNumberField: EditText
    lateinit var passwordField: EditText
    lateinit var createAccountButton: TextView
    lateinit var mainWindowProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginButton)
        accountNumberField = findViewById(R.id.accountNumberField)
        passwordField = findViewById(R.id.passwordField)
        createAccountButton = findViewById(R.id.createAccountButton)
        mainWindowProgressBar = findViewById(R.id.mainWindowProgressBar)

        loginButton.setOnClickListener {
            when {
                accountNumberField.text.toString() == "" -> {
                    Toast.makeText(this, getString(R.string.account_number_missing), Toast.LENGTH_SHORT)
                        .show()
                }

                passwordField.text.toString() == "" -> {
                    Toast.makeText(this, getString(R.string.password_missing), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    changeButtonState(true)

                    AccountsManager.delay(1000) {
                        val loginResult = AccountsManager.authenticate(
                            accountNumberField.text.toString(),
                            passwordField.text.toString(),
                            this
                        )

                        if (loginResult != null) {
                            // Pass account to new screen, make it serializable.
                            val intent = Intent(this, AccountScreenActivity::class.java).apply {
                                putExtra(LOGGED_USER, loginResult)
                            }
                            startActivity(intent)
                            finish()

                        } else {
                            changeButtonState(false)
                            Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
        createAccountButton.setOnClickListener {
            changeButtonState(true)

            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)

            changeButtonState(false)
        }
    }

    private fun changeButtonState(isLoading: Boolean) {
        if (isLoading) {
            loginButton.apply {
                alpha = 0.5F
                isClickable = false
                isFocusable = false
            }

            createAccountButton.apply {
                isClickable = false
                isFocusable = false
            }

            mainWindowProgressBar.visibility = View.VISIBLE

        } else {
            loginButton.apply {
                alpha = 1F
                isClickable = true
                isFocusable = true
            }
            createAccountButton.apply {
                isClickable = true
                isFocusable = true
            }

            mainWindowProgressBar.visibility = View.INVISIBLE
        }
    }
}
package com.alexparra.bankaccountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.alexparra.bankaccountapp.objects.AccountsManager

const val LOGGED_USER = "LOGGED_USER"

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var accountNumberField: EditText
    private lateinit var passwordField: EditText
    private lateinit var createAccountButton: TextView
    private lateinit var mainWindowProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginButton)
        accountNumberField = findViewById(R.id.accountNumberField)
        passwordField = findViewById(R.id.passwordField)
        createAccountButton = findViewById(R.id.createAccountButton)
        mainWindowProgressBar = findViewById(R.id.mainWindowProgressBar)

        // Check current session.
        val sessionUser = AccountsManager.checkSession()
        if (sessionUser != null) {
            val intent = Intent(MainApplication.applicationContext(), AccountScreenActivity::class.java).apply {
                putExtra(LOGGED_USER, sessionUser)
            }
            startActivity(intent)
        }

        initializeButtons()
    }

    private fun initializeButtons() {

        // Button actions.
        loginButton.setOnClickListener {
            when {
                accountNumberField.text.toString() == "" -> {
                    Toast.makeText(
                        this,
                        getString(R.string.account_number_missing),
                        Toast.LENGTH_SHORT
                    )
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

                            // Save current logged session.
                            AccountsManager.saveSession(accountNumberField.text.toString())

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
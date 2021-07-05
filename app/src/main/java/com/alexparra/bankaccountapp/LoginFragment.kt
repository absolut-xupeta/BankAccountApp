package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.AccountScreenFragment
import com.alexparra.bankaccountapp.utils.addFragmentWithArgs
import com.alexparra.bankaccountapp.utils.replaceFragment

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginButton: Button
    private lateinit var accountNumberField: EditText
    private lateinit var passwordField: EditText
    private lateinit var createAccountButton: TextView
    private lateinit var mainWindowProgressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view.apply {
            loginButton = findViewById(R.id.loginButton)
            accountNumberField = findViewById(R.id.accountNumberField)
            passwordField = findViewById(R.id.passwordField)
            createAccountButton = findViewById(R.id.createAccountButton)
            mainWindowProgressBar = findViewById(R.id.mainWindowProgressBar)
        }

        initializeButtons()
    }

    private fun initializeButtons() {

        // Button actions.
        loginButton.setOnClickListener {
            when {
                accountNumberField.text.toString() == "" -> {
                    Toast.makeText(
                        context,
                        getString(R.string.account_number_missing),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                passwordField.text.toString() == "" -> {
                    Toast.makeText(context, getString(R.string.password_missing), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    changeButtonState(true)

                    AccountsManager.delay(1000) {
                        val loginResult = AccountsManager.authenticate(
                            accountNumberField.text.toString(),
                            passwordField.text.toString(),
                            requireContext()
                        )

                        if (loginResult != null) {
                            // Pass account to new screen, make it serializable.
                                // TODO REMOVE THIS
//                            val intent = Intent(context, AccountScreenActivity::class.java).apply {
//                                putExtra(SESSION_USER, loginResult)
//                            }

                            // Save current logged session.
                            AccountsManager.saveSession(accountNumberField.text.toString())

                            // TODO
                            AccountScreenFragment().addFragmentWithArgs(SESSION_USER, loginResult)

                        } else {
                            changeButtonState(false)
                            Toast.makeText(context, getString(R.string.login_error), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }

        createAccountButton.setOnClickListener {
            changeButtonState(true)

            // TODO
            CreateAccountFragment().replaceFragment()

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
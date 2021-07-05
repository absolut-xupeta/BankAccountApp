package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.alexparra.bankaccountapp.objects.AccountsManager
import java.util.*

class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    lateinit var accountCreation: TextView
    lateinit var nameCreation: EditText
    lateinit var passwordCreation: EditText
    lateinit var chooseSavings: RadioButton
    lateinit var chooseCurrent: RadioButton
    lateinit var createAccountButton: Button
    lateinit var initialDeposit: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view.apply {
            accountCreation = findViewById(R.id.accountCreation)
            nameCreation = findViewById(R.id.nameCreation)
            passwordCreation = findViewById(R.id.passwordCreation)
            chooseSavings = findViewById(R.id.chooseSavings)
            chooseCurrent = findViewById(R.id.chooseCurrent)
            createAccountButton = findViewById(R.id.createAccountButton)
            initialDeposit = findViewById(R.id.initialDeposit)
        }

        initViews()
    }

    private fun initViews() {
        val id = AccountsManager.generateAccountNumber(requireContext())
        accountCreation.text = id

        createAccountButton.setOnClickListener {

            if (nameCreation.text.toString() == "" || passwordCreation.text.toString() == "") {
                Toast.makeText(context, R.string.field_warning, Toast.LENGTH_SHORT).show()

            } else if (!chooseSavings.isChecked && !chooseCurrent.isChecked) {
                Toast.makeText(context, R.string.radio_button_warning, Toast.LENGTH_SHORT).show()

            } else {
                changeButtonState(true)

                // Check for the initial deposit
                val balance: Long = if (initialDeposit.text.toString() == "") {
                    0
                } else {
                    val value = initialDeposit.text.toString()
                    value.toLong() * 100
                }

                // Check the user account type
                val type: String = if (chooseSavings.isChecked) {
                    AccountsManager.SAVINGS
                } else {
                    AccountsManager.CURRENT
                }

                // Get the creation date.
                val date = Calendar.getInstance().timeInMillis.toString()

                // Create the new user.
                val result = AccountsManager.createAccount(
                    requireContext(),
                    type,
                    id,
                    passwordCreation.text.toString(),
                    nameCreation.text.toString(),
                    date,
                    balance,
                )

                // Go back to the login screen.
                if (result) {
                    Toast.makeText(context, R.string.create_account_success, Toast.LENGTH_SHORT).show()
                    AccountsManager.delay {
                        // TODO
                    // finish()
                    }
                } else {
                    Toast.makeText(context, R.string.same_account_type, Toast.LENGTH_LONG).show()
                    changeButtonState(false)
                }
            }
        }
    }

    private fun changeButtonState(isLoading: Boolean) {
        if (isLoading) {
            createAccountButton.apply {
                alpha = 1F
                isClickable = false
                isFocusable = false
            }
        } else {
            createAccountButton.apply {
                alpha = 0.5F
                isClickable = true
                isFocusable = true
            }
        }
    }
}
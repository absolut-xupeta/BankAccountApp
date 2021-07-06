package com.alexparra.bankaccountapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

const val VALUE = "VALUE"
const val OPERATION_TYPE = "OPERATION"

class DepositWithdrawActivity : Fragment(R.layout.fragment_deposit_withdraw) {
    lateinit var header: TextView
    lateinit var operationAmount: EditText
    lateinit var confirmOperationButton: Button

    override fun onViewCreate(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view.apply {
            header = findViewById(R.id.header)
            operationAmount = findViewById(R.id.operationAmount)
            confirmOperationButton = findViewById(R.id.confirmOperationButton)
        }

        // TODO REFACTOR THIS
        val operation = intent.getStringExtra(OPERATION)

        // Set text for the right operation.
        header.text = operation
        confirmOperationButton.text = operation

        // String for the amount = 0 warning.
        val warning = "${getString(R.string.amount_warning)} $operation"

        // Discover the operation type to return.
        val operationType = if (operation == "Withdraw") "Withdraw" else "Deposit"

        // Button click.
        confirmOperationButton.setOnClickListener {
            if (operationAmount.text.toString() == "") {
                Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()

            } else {
                val intent = Intent().apply {
                    putExtra(VALUE, operationAmount.text.toString())
                    putExtra(OPERATION_TYPE, operationType)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
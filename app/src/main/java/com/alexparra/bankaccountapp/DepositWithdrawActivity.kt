package com.alexparra.bankaccountapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

const val VALUE = "VALUE"
const val OPERATION_TYPE = "OPERATION"

class DepositWithdrawActivity : AppCompatActivity() {
    lateinit var header: TextView
    lateinit var operationAmount: EditText
    lateinit var confirmOperationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_withdraw)

        header = findViewById(R.id.header)
        operationAmount = findViewById(R.id.operationAmount)
        confirmOperationButton = findViewById(R.id.confirmOperationButton)

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
                Toast.makeText(this, warning, Toast.LENGTH_LONG).show()

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
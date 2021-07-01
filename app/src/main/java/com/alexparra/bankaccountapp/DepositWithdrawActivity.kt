package com.alexparra.bankaccountapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.alexparra.bankaccountapp.model.Account

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

        val operation = intent.getStringExtra(OPERATION)

        // Set text for the right operation.
        header.text = operation
        confirmOperationButton.text = operation

        // String for the amount = 0 warning.
        val warning = "${R.string.amount_warning} $operation"

        confirmOperationButton.setOnClickListener {
            if (operationAmount.text.toString() == "") {
                Toast.makeText(this, warning, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "done", Toast.LENGTH_LONG).show()
                //TODO CREATE FILE UPDATER FOR THE AMOUNT
            }
        }
    }
}
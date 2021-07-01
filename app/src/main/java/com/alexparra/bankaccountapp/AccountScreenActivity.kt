package com.alexparra.bankaccountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import java.text.DateFormat.getDateInstance
import java.util.*

const val OPERATION = "OPERATION"

class AccountScreenActivity : AppCompatActivity() {
    lateinit var bankUserName: TextView
    lateinit var accountType: TextView
    lateinit var currencyAmount: TextView
    lateinit var creationDate: TextView
    lateinit var withdraw: ImageView
    lateinit var deposit: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_screen)

        bankUserName = findViewById(R.id.bankUserName)
        accountType = findViewById(R.id.accountType)
        currencyAmount = findViewById(R.id.currencyAmount)
        creationDate = findViewById(R.id.creationDate)
        withdraw = findViewById(R.id.withdraw)
        deposit = findViewById(R.id.deposit)

        // Get Account object from MainActivity.
        val user = intent.getSerializableExtra(LOGGED_USER) as? Account

        // Transform date.
        val date = user?.creationDate ?: "Nothing"
        val dateFormat = getDateInstance()
        val finalDate = dateFormat.format(date)

        // Put the owner name to Camel Case.
        val userFinalName = user?.ownerName?.split(' ')?.joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() }}

        // Transform values to better visualization.
        val transformBalance = user?.balance?.div(100).toString()
        val balance = "${getString(R.string.brl)} $transformBalance"

        bankUserName.text = userFinalName
        accountType.text = if (user is CurrentAccount) "Current Account" else "Savings Account"
        currencyAmount.text = balance
        creationDate.text = finalDate.toString()

        // Button actions
        withdraw.setOnClickListener {
            callIntent(getString(R.string.withdraw))
        }

        deposit.setOnClickListener {
            callIntent(getString(R.string.deposit))
        }
    }
    private fun callIntent(operation: String) {
        val intent = Intent(this, DepositWithdrawActivity::class.java).apply {
            putExtra(OPERATION, operation)
        }
        startActivity(intent)
    }
}

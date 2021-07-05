package com.alexparra.bankaccountapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import java.lang.Exception
import java.text.DateFormat.getDateInstance

const val OPERATION = "OPERATION"

class AccountScreenActivity : AppCompatActivity() {

    // Handle the deposit/withdraw return from DepositWithdrawActivity.
    val resultValue = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newBalance = result.data?.getStringExtra(VALUE)?.toLong()
            val operationMade = result.data?.getStringExtra(OPERATION_TYPE)

            if (newBalance != null && operationMade != null) {
                updateBalance(newBalance, operationMade)
            }

        } else {
            Toast.makeText(this, R.string.transfer_error, Toast.LENGTH_LONG).show()
        }
    }

    lateinit var logoutButton: TextView
    lateinit var bankUserName: TextView
    lateinit var accountType: TextView
    lateinit var currencyAmount: TextView
    lateinit var creationDate: TextView
    lateinit var withdraw: ImageView
    lateinit var deposit: ImageView

    lateinit var user: Account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_screen)

        logoutButton = findViewById(R.id.logoutButton)
        bankUserName = findViewById(R.id.bankUserName)
        accountType = findViewById(R.id.accountType)
        currencyAmount = findViewById(R.id.currencyAmount)
        creationDate = findViewById(R.id.creationDate)
        withdraw = findViewById(R.id.withdraw)
        deposit = findViewById(R.id.deposit)


        // Get Account object from MainActivity.
        user = intent.getSerializableExtra(LOGGED_USER) as Account? ?: throw Exception("Missing user object.")

        // Transform date.
        val date = user.creationDate
        val dateFormat = getDateInstance()
        val finalDate = dateFormat.format(date)

        // Put the owner name to Camel Case.
        val userFinalName = user.ownerName.split(' ').joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() }}

        // Transform values to better visualization.
        bankUserName.text = userFinalName
        accountType.text = if (user is CurrentAccount) "Current Account" else "Savings Account"
        currencyAmount.text = getBalanceString(user)
        creationDate.text = finalDate.toString()

        // Button actions
        withdraw.setOnClickListener {
            val intent = createIntent(getString(R.string.withdraw))
            resultValue.launch(intent)
        }

        deposit.setOnClickListener {
            val intent = createIntent(getString(R.string.deposit))
            resultValue.launch(intent)
        }

        logoutButton.setOnClickListener {
            AccountsManager.clearSession()
            finish()
        }
    }

    private fun createIntent(type: String): Intent {
        return Intent(this, DepositWithdrawActivity::class.java).apply {
            putExtra(OPERATION, type)
        }
    }

    private fun getBalanceString(user: Account?): String {
        val transformBalance = user?.balance?.div(100).toString()
        return "${getString(R.string.brl)} $transformBalance"
    }

    private fun updateBalance(value: Long, operationType: String) {
        val newValue = value * 100

        if (operationType == "Deposit") {
            user.balance = user.balance.plus(newValue)
            Toast.makeText(this, R.string.deposit_complete, Toast.LENGTH_LONG).show()
            currencyAmount.text = getBalanceString(user)
            AccountsManager.updateUser(this, user)

        } else {
            if (user.balance.minus(newValue) < 0) {
                // ERROR
                Toast.makeText(this, R.string.withdraw_error, Toast.LENGTH_LONG).show()

            } else {
                user.balance = user.balance.minus(newValue)
                Toast.makeText(this, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                currencyAmount.text = getBalanceString(user)
                AccountsManager.updateUser(this, user)
            }
        }
    }
}

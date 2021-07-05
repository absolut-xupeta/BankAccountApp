package com.alexparra.bankaccountapp.utils

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alexparra.bankaccountapp.*
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import java.text.DateFormat

class AccountScreenFragment : Fragment(R.layout.fragment_account) {

    // Handle the deposit/withdraw return from DepositWithdrawActivity.
//    val resultValue = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val newBalance = result.data?.getStringExtra(VALUE)?.toLong()
//            val operationMade = result.data?.getStringExtra(OPERATION_TYPE)
//
//            if (newBalance != null && operationMade != null) {
//                updateBalance(newBalance, operationMade)
//            }
//
//        } else {
//            Toast.makeText(context, R.string.transfer_error, Toast.LENGTH_LONG).show()
//        }
//    }

    lateinit var logoutButton: TextView
    lateinit var bankUserName: TextView
    lateinit var accountType: TextView
    lateinit var currencyAmount: TextView
    lateinit var creationDate: TextView
    lateinit var withdraw: ImageView
    lateinit var deposit: ImageView

    lateinit var user: Account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            logoutButton = findViewById(R.id.logoutButton)
            bankUserName = findViewById(R.id.bankUserName)
            accountType = findViewById(R.id.accountType)
            currencyAmount = findViewById(R.id.currencyAmount)
            creationDate = findViewById(R.id.creationDate)
            withdraw = findViewById(R.id.withdraw)
            deposit = findViewById(R.id.deposit)
        }

        // TODO REMOVE THIS
        // Get Account object from MainActivity.
//        user = intent.getSerializableExtra(LOGGED_USER) as Account?
//            ?: throw Exception("Missing user object.")
        user = requireArguments().getSerializable(SESSION_USER) as Account

        // Transform date.
        val date = user.creationDate
        val dateFormat = DateFormat.getDateInstance()
        val finalDate = dateFormat.format(date)

        // Put the owner name to Camel Case.
        val userFinalName = user.ownerName.split(' ')
            .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

        // Transform values to better visualization.
        bankUserName.text = userFinalName
        accountType.text = if (user is CurrentAccount) "Current Account" else "Savings Account"
        currencyAmount.text = getBalanceString(user)
        creationDate.text = finalDate.toString()

        // Button actions
        withdraw.setOnClickListener {
            // TODO GO TO NEW FRAGMENT

        }

        deposit.setOnClickListener {
            // TODO GO TO NEW FRAGMENT
        }

        logoutButton.setOnClickListener {
            AccountsManager.clearSession()
            // TODO CHANGE TO LOGIN SCREEN AGAIN
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
            Toast.makeText(context, R.string.deposit_complete, Toast.LENGTH_LONG).show()
            currencyAmount.text = getBalanceString(user)
            AccountsManager.updateUser(requireContext(), user)

        } else {
            if (user.balance.minus(newValue) < 0) {
                // ERROR
                Toast.makeText(context, R.string.withdraw_error, Toast.LENGTH_LONG).show()

            } else {
                user.balance = user.balance.minus(newValue)
                Toast.makeText(context, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                currencyAmount.text = getBalanceString(user)
                AccountsManager.updateUser(requireContext(), user)
            }
        }
    }
}

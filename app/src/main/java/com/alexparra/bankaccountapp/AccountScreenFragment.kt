package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexparra.bankaccountapp.databinding.FragmentAccountBinding
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import java.text.DateFormat

const val USER = "USER"
const val TRANSACTION = "TRANSACTION"

class AccountScreenFragment : Fragment() {

    private val args: AccountScreenFragmentArgs by navArgs()

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Listen to the value returned by the DepositWithdrawFragment.
        setFragmentResultListener(TRANSACTION) { _, bundle ->
            val value = bundle.getString(VALUE) as String
            val operation = bundle.getString(OPERATION) as String

            updateBalance(value.toLong(), operation)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Transform date.
        val date = args.user.creationDate
        val dateFormat = DateFormat.getDateInstance()
        val finalDate = dateFormat.format(date)

        // Put the owner name to Camel Case.
        val userFinalName = args.user.ownerName.split(' ')
            .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

        binding.apply {
            // Transform values to better visualization.
            bankUserName.text = userFinalName
            accountType.text = if (args.user is CurrentAccount) "Current Account" else "Savings Account"
            currencyAmount.text = getBalanceString(args.user)
            creationDate.text = finalDate.toString()

            // Button actions
            withdraw.setOnClickListener {
                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToDepositWithdrawFragment("Withdraw")
                findNavController().navigate(action)
            }

            deposit.setOnClickListener {
                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToDepositWithdrawFragment("Deposit")
                findNavController().navigate(action)
            }

            logoutButton.setOnClickListener {
                AccountsManager.clearSession()

                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun getBalanceString(user: Account?): String {
        val transformBalance = user?.balance?.div(100).toString()
        return "${getString(R.string.brl)} $transformBalance"
    }

    private fun updateBalance(value: Long, operationType: String) {
        val newValue = value * 100

        if (operationType == "Deposit") {
            args.user.balance = args.user.balance.plus(newValue)
            Toast.makeText(context, R.string.deposit_complete, Toast.LENGTH_LONG).show()
            binding.currencyAmount.text = getBalanceString(args.user)
            AccountsManager.updateUser(requireContext(), args.user)

        } else {
            if (args.user.balance.minus(newValue) < 0) {
                // ERROR
                Toast.makeText(context, R.string.withdraw_error, Toast.LENGTH_LONG).show()

            } else {
                args.user.balance = args.user.balance.minus(newValue)
                Toast.makeText(context, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                binding.currencyAmount.text = getBalanceString(args.user)
                AccountsManager.updateUser(requireContext(), args.user)
            }
        }
    }
}
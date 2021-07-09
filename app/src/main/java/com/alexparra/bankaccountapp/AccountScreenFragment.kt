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
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import java.text.DateFormat

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
            val id = bundle.getString(ID)

            val result = AccountsManager.updateBalance(id, args.user, value.toLong(), operation)

            if (result) {
                when (operation) {
                    "Deposit" -> {
                        Toast.makeText(context, R.string.deposit_complete, Toast.LENGTH_LONG).show()
                        updateBalanceView(value, "Deposit")
                    }

                    "Withdraw" -> {
                        Toast.makeText(context, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                        updateBalanceView(value, "Withdraw")
                    }

                    else -> {
                        Toast.makeText(context, R.string.transfer_complete, Toast.LENGTH_LONG).show()
                        updateBalanceView(value, "Transfer")
                    }
                }

            } else {
                if (operation == "Withdraw") {
                    Toast.makeText(context, R.string.withdraw_error, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, R.string.transfer_enough_amount, Toast.LENGTH_LONG).show()
                }
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    /**
     * Initialize this fragment buttons.
     */
    private fun initButtons() {

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
            currencyAmount.text = getBalanceString(args.user.balance)
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

            transaction.setOnClickListener {
                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToTransactionFragment()
                findNavController().navigate(action)
            }

            transfer.setOnClickListener {
                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToTransferFragment()
                findNavController().navigate(action)
            }

            logoutButton.setOnClickListener {
                AccountsManager.clearSession()

                val action = AccountScreenFragmentDirections.actionAccountScreenFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    /**
     * Transform the Long value to a balance compatible Int and
     * concat into a string to be displayed on a TextView.
     */
    private fun getBalanceString(value: Long): String {
        val newValue = value.div(100).toInt()
        return transformToBalanceString(newValue)
    }

    /**
     * Return the concat text to be used on TextView.
     */
    private fun transformToBalanceString(value: Int): String {
        return "${getString(R.string.brl)} $value"
    }

    /**
     * Transforms the current TextView value to an Int and return it.
     */
    private fun getBalanceInt(): Int {
        return binding.currencyAmount.text.toString().filter { it.isDigit() }.toInt()
    }

    /**
     * Update the screen TextView balance display based on the last operation that
     * take place with this account.
     */
    private fun updateBalanceView(amount: String, operation: String) {
        val viewValue = getBalanceInt()

        if (operation == "Deposit") {
            val newValue = viewValue + amount.toInt()
            binding.currencyAmount.text = transformToBalanceString(newValue)
        } else {
            val newValue = viewValue - amount.toInt()
            binding.currencyAmount.text = transformToBalanceString(newValue)
        }
    }
}
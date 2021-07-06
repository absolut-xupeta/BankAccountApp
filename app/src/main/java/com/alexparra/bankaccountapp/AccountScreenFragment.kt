package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.alexparra.bankaccountapp.*
import com.alexparra.bankaccountapp.databinding.FragmentAccountBinding
import com.alexparra.bankaccountapp.databinding.FragmentLoginBinding
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.replaceFragment
import java.lang.Exception
import java.text.DateFormat

const val USER = "USER"
const val TRANSACTION = "TRANSACTION"

class AccountScreenFragment : Fragment() {

    private lateinit var user: Account

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

        arguments?.apply {
            user = getSerializable(USER) as Account? ?: throw Exception("Missing account object.")
        }

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
        val date = user.creationDate
        val dateFormat = DateFormat.getDateInstance()
        val finalDate = dateFormat.format(date)

        // Put the owner name to Camel Case.
        val userFinalName = user.ownerName.split(' ')
            .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

        binding.apply {
            // Transform values to better visualization.
            bankUserName.text = userFinalName
            accountType.text = if (user is CurrentAccount) "Current Account" else "Savings Account"
            currencyAmount.text = getBalanceString(user)
            creationDate.text = finalDate.toString()

            // Button actions
            withdraw.setOnClickListener {
                replaceFragment(DepositWithdrawFragment.newInstance("Withdraw"), R.id.fragment_container_view)
            }

            deposit.setOnClickListener {
                replaceFragment(DepositWithdrawFragment.newInstance("Deposit"), R.id.fragment_container_view)
            }

            logoutButton.setOnClickListener {
                AccountsManager.clearSession()
                replaceFragment(LoginFragment.newInstance(), R.id.fragment_container_view)
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
            user.balance = user.balance.plus(newValue)
            Toast.makeText(context, R.string.deposit_complete, Toast.LENGTH_LONG).show()
            binding.currencyAmount.text = getBalanceString(user)
            AccountsManager.updateUser(requireContext(), user)

        } else {
            if (user.balance.minus(newValue) < 0) {
                // ERROR
                Toast.makeText(context, R.string.withdraw_error, Toast.LENGTH_LONG).show()

            } else {
                user.balance = user.balance.minus(newValue)
                Toast.makeText(context, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                binding.currencyAmount.text = getBalanceString(user)
                AccountsManager.updateUser(requireContext(), user)
            }
        }
    }

        companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param user The user that logged in.
         * @return A new instance of fragment Test1Fragment.
         */
        @JvmStatic
        fun newInstance(user: Account) =
            AccountScreenFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(USER, user)
                }
            }
    }
}
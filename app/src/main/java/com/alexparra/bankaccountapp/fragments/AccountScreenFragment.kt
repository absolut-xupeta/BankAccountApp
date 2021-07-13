package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.adapters.ServicesAdapter
import com.alexparra.bankaccountapp.databinding.FragmentAccountBinding
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.objects.AccountsManager.formatMoneyBalance
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
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
                        updateBalanceView("Deposit")
                    }

                    "Withdraw" -> {
                        Toast.makeText(context, R.string.withdraw_complete, Toast.LENGTH_LONG).show()
                        updateBalanceView("Withdraw")
                    }

                    else -> {
                        Toast.makeText(context, R.string.transfer_complete, Toast.LENGTH_LONG).show()
                        updateBalanceView("Transfer")
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
            currencyAmount.text = formatMoneyBalance(args.user.balance)
            creationDate.text = finalDate.toString()

            //NavigationBarView.OnItemSelectedListener()

            val list = ArrayList<String>()
            list.add("deposit")
            list.add("withdraw")
            list.add("transfer")
            list.add("transaction")
            list.add("investments")

            val recyclerViewList: RecyclerView = binding.accountRecycler
            val servicesAdapter = ServicesAdapter(list) {
                when (it) {
                    "deposit" -> {
                        val action = AccountScreenFragmentDirections
                            .actionAccountScreenFragmentToDepositWithdrawFragment("Deposit")

                        findNavController().navigate(action)
                    }


                    "withdraw" -> {
                        val action = AccountScreenFragmentDirections
                            .actionAccountScreenFragmentToDepositWithdrawFragment("Withdraw")

                        findNavController().navigate(action)
                    }


                    "transfer" -> {
                        val action = AccountScreenFragmentDirections
                            .actionAccountScreenFragmentToTransferFragment()

                        findNavController().navigate(action)
                    }


                    "transaction" -> {
                        val action = AccountScreenFragmentDirections
                            .actionAccountScreenFragmentToTransactionFragment(args.user.accountNumber.toString())

                        findNavController().navigate(action)
                    }

                    "logout" -> {
                        AccountsManager.clearSession()
                        findNavController().popBackStack()
                    }
                }
            }

            recyclerViewList.apply {
                adapter = servicesAdapter
                layoutManager = GridLayoutManager(context, 2)
            }


            logoutButton.setOnClickListener {
                AccountsManager.clearSession()

                findNavController().popBackStack()
            }
        }
    }

    /**
     * Update the screen TextView balance display based on the last operation that
     * take place with this account.
     */
    private fun updateBalanceView(operation: String) {

        if (operation == "Deposit") {
            binding.currencyAmount.text = formatMoneyBalance(args.user.balance)
        } else {
            binding.currencyAmount.text = formatMoneyBalance(args.user.balance)
        }
    }
}
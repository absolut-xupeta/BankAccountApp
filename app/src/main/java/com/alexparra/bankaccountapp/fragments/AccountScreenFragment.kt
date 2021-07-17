package com.alexparra.bankaccountapp.fragments

import android.content.ContentResolver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.adapters.ServicesAdapter
import com.alexparra.bankaccountapp.databinding.FragmentAccountBinding
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.Services
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.objects.AccountsManager.formatMoneyBalance
import com.alexparra.bankaccountapp.utils.toast
import java.text.DateFormat

const val TRANSACTION = "TRANSACTION"

class AccountScreenFragment : Fragment() {

    private val args: AccountScreenFragmentArgs by navArgs()

    private lateinit var binding: FragmentAccountBinding

    private val navController: NavController by lazy {
        findNavController()
    }

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
                        toast(getString(R.string.deposit_complete))
                        updateBalanceView("Deposit")
                    }

                    "Withdraw" -> {
                        toast(getString(R.string.withdraw_complete))
                        updateBalanceView("Withdraw")
                    }

                    else -> {
                        toast(getString(R.string.transfer_complete))
                        updateBalanceView("Transfer")
                    }
                }

            } else {
                if (operation == "Withdraw") {
                    toast(getString(R.string.withdraw_error))
                } else {
                    toast(getString(R.string.transfer_enough_amount))
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

        with(binding) {
            // Transform values to better visualization.
            bankUserName.text = userFinalName
            accountType.text =
                if (args.user is CurrentAccount) "Current Account" else "Savings Account"
            currencyAmount.text = formatMoneyBalance(args.user.balance)
            creationDate.text = finalDate.toString()

            val list = getServices()


            val recyclerViewList: RecyclerView = binding.accountRecycler

            val servicesAdapter = ServicesAdapter(list, ::handleAdapter)

            recyclerViewList.apply {
                adapter = servicesAdapter
                layoutManager = GridLayoutManager(context, 2)
            }

            logoutButton.setOnClickListener {
                AccountsManager.clearSession()

                val action =
                    AccountScreenFragmentDirections.actionAccountScreenFragmentToSplashFragment()
                navController.navigate(action)
            }
        }
    }

    private fun handleAdapter(service: Services) {
        when (service.text) {
            "Deposit" -> {
                val action = AccountScreenFragmentDirections
                    .actionAccountScreenFragmentToDepositWithdrawFragment("Deposit")

                navController.navigate(action)
            }

            "Withdraw" -> {
                val action = AccountScreenFragmentDirections
                    .actionAccountScreenFragmentToDepositWithdrawFragment("Withdraw")

                navController.navigate(action)
            }

            "Transfer" -> {
                val action = AccountScreenFragmentDirections
                    .actionAccountScreenFragmentToTransferFragment()

                navController.navigate(action)
            }

            "Transaction" -> {
                val action = AccountScreenFragmentDirections
                    .actionAccountScreenFragmentToTransactionFragment(args.user.accountNumber.toString())

                navController.navigate(action)
            }

            "Tic Tac Toe" -> {
                val action =
                    AccountScreenFragmentDirections.actionAccountScreenFragmentToTicTacToeFragment()

                navController.navigate(action)
            }

        }
    }

    /**
     * Update the screen TextView balance display based on the last operation that
     * took place with this account.
     */
    private fun updateBalanceView(operation: String) {

        if (operation == "Deposit") {
            binding.currencyAmount.text = formatMoneyBalance(args.user.balance)
        } else {
            binding.currencyAmount.text = formatMoneyBalance(args.user.balance)
        }
    }

    private fun getServices(): ArrayList<Services> {
        val list = ArrayList<Services>()

        ContentResolver.getCurrentSyncs()

        list.add(Services(getString(R.string.grid_deposit), R.drawable.ic_deposit))
        list.add(Services(getString(R.string.grid_withdraw), R.drawable.ic_withdraw))
        list.add(Services(getString(R.string.grid_transfer), R.drawable.ic_transfer))
        list.add(Services(getString(R.string.grid_transaction), R.drawable.ic_transaction))
        list.add(Services(getString(R.string.grid_investments), R.drawable.ic_investments))
        list.add(Services(getString(R.string.tic_tac_toe), R.drawable.ic_tic_tac_toe))

        return list
    }
}
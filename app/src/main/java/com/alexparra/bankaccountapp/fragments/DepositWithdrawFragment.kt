package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.FragmentDepositWithdrawBinding

const val VALUE = "VALUE"
const val OPERATION = "OPERATION"

class DepositWithdrawFragment : Fragment() {

    private val args: DepositWithdrawFragmentArgs by navArgs()

    private lateinit var binding: FragmentDepositWithdrawBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositWithdrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Set text for the right operation.
            header.text = args.operation
            confirmOperationButton.text = args.operation

            // String for the amount = 0 warning.
            val warning = "${getString(R.string.amount_warning)} ${args.operation}"

            // Discover the operation type to return.
            val operationType = if (args.operation == "Withdraw") "Withdraw" else "Deposit"

            // Button click.
            confirmOperationButton.setOnClickListener {
                if (operationAmount.text.toString() == "") {
                    Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()

                } else {

                    setFragmentResult(
                        TRANSACTION,
                        bundleOf(VALUE to operationAmount.text.toString(), OPERATION to operationType)
                    )
                    navController.popBackStack()
                }
            }
        }
    }
}
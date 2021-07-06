package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.alexparra.bankaccountapp.databinding.FragmentDepositWithdrawBinding
import com.alexparra.bankaccountapp.utils.popBackStack

const val VALUE = "VALUE"
const val OPERATION = "OPERATION"

class DepositWithdrawFragment : Fragment() {

    private lateinit var binding: FragmentDepositWithdrawBinding

    private lateinit var operation: String

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

        arguments?.apply {
            operation = getString(OPERATION) as String
        }


        binding.apply {
            // Set text for the right operation.
            header.text = operation
            confirmOperationButton.text = operation

            // String for the amount = 0 warning.
            val warning = "${getString(R.string.amount_warning)} $operation"

            // Discover the operation type to return.
            val operationType = if (operation == "Withdraw") "Withdraw" else "Deposit"

            // Button click.
            confirmOperationButton.setOnClickListener {
                if (operationAmount.text.toString() == "") {
                    Toast.makeText(requireContext(), warning, Toast.LENGTH_LONG).show()

                } else {
                    setFragmentResult(
                        TRANSACTION,
                        bundleOf(VALUE to operationAmount.text.toString(), OPERATION to operationType)
                    )

                    popBackStack()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param operation The operation type the user will perform (Deposit or Withdraw).
         * @return A new instance of fragment DepositWithDrawFragment.
         */
        @JvmStatic
        fun newInstance(operation: String) =
            DepositWithdrawFragment().apply {
                arguments = Bundle().apply {
                    putString(OPERATION, operation)
                }
            }
    }
}
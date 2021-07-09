package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.alexparra.bankaccountapp.databinding.FragmentTransferBinding
import com.alexparra.bankaccountapp.objects.AccountsManager

const val ID = "ID"

class TransferFragment : Fragment() {

    private lateinit var binding: FragmentTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transfer()
    }

    private fun transfer() {

        binding.confirmOperationButton.setOnClickListener {
            when {
                binding.accountId.text.toString() == "" -> Toast.makeText(requireContext(), R.string.no_id_warning, Toast.LENGTH_LONG).show()

                binding.transferAmount.text.toString() == "" -> Toast.makeText(requireContext(), R.string.transfer_amount_zero, Toast.LENGTH_LONG).show()

                else -> {
                    val idToTransfer = binding.accountId.text.toString()
                    val transferAmount = binding.transferAmount.text.toString()

                    val searchId: Boolean = AccountsManager.searchUser(idToTransfer)

                    if (searchId) {
                        setFragmentResult(
                            TRANSACTION,
                            bundleOf(VALUE to transferAmount, OPERATION to "Transfer", ID to idToTransfer)
                        )
                        findNavController().popBackStack()

                    } else {
                        Toast.makeText(requireContext(), R.string.account_missing, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
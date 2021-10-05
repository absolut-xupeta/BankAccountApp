package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.FragmentTransferBinding
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.toast

const val ID = "ID"

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding

    private val navController: NavController by lazy {
        findNavController()
    }

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
                binding.accountId.text.toString() == "" -> toast(getString(R.string.no_id_warning))

                binding.transferAmount.text.toString() == "" -> toast(getString(R.string.transfer_amount_zero))

                else -> {
                    val idToTransfer = binding.accountId.text.toString()
                    val transferAmount = binding.transferAmount.text.toString()

                    val searchId: Boolean = AccountsManager.searchUser(idToTransfer)

                    if (searchId) {
                        setFragmentResult(
                            TRANSACTION,
                            bundleOf(VALUE to transferAmount, OPERATION to "Transfer", ID to idToTransfer)
                        )
                        navController.popBackStack()

                    } else {
                        toast(getString(R.string.account_missing))
                    }
                }
            }
        }
    }
}
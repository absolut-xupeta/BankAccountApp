package com.alexparra.bankaccountapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.databinding.FragmentLoginBinding
import com.alexparra.bankaccountapp.databinding.FragmentTransactionBinding
import com.alexparra.bankaccountapp.model.CustomAdapter
import com.alexparra.bankaccountapp.objects.AccountsManager

class TransactionFragment : Fragment() {

    private val args: TransactionFragmentArgs by navArgs()

    private lateinit var binding: FragmentTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView()
    }

    /**
     * Display recycler view if transaction history is not null.
     */
    private fun recyclerView() {
        val transactions = AccountsManager.loadAccountTransaction(args.id)

        if (transactions != null) {
            val recyclerView: RecyclerView = binding.recycler
            val customAdapter = CustomAdapter(transactions)

            recyclerView.apply {
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = customAdapter
                layoutManager = LinearLayoutManager(context)
            }

        } else {
            displayNothing()
        }
    }

    /**
     * If the recyclerView is empty display only a warning message.
     */
    private fun displayNothing() {
        binding.recycler.apply {
            visibility = View.GONE
        }

        binding.noTransaction.apply {
            visibility = View.VISIBLE
        }
    }
}
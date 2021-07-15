package com.alexparra.bankaccountapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.AccountViewBinding

class ServicesAdapter(
    private val dataSet: ArrayList<String>,
    private val onClick: (String) -> Unit
) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private lateinit var binding: AccountViewBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val binding: AccountViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onClick: (String) -> Unit) {
            with(binding) {
                when (item) {
                    "deposit" -> {
                        actionName.text = root.context.getString(R.string.grid_deposit)
                        actionImage.setImageResource(R.drawable.ic_deposit)
                    }

                    "withdraw" -> {
                        actionName.text = root.context.getString(R.string.grid_withdraw)
                        actionImage.setImageResource(R.drawable.ic_withdraw)
                    }

                    "transfer" -> {
                        actionName.text = root.context.getString(R.string.grid_transfer)
                        actionImage.setImageResource(R.drawable.ic_transfer)
                    }

                    "transaction" -> {
                        actionName.text = root.context.getString(R.string.grid_transaction)
                        actionImage.setImageResource(R.drawable.ic_transaction)
                    }

                    "investments" -> {
                        operationView.isClickable = false
                        operationView.isFocusable = false
                        actionName.text = root.context.getString(R.string.grid_investments)
                        actionImage.setImageResource(R.drawable.ic_investments)
                        warning.visibility = View.VISIBLE
                        warning.text = root.context.getString(R.string.soon_warning)
                    }

                    "ticTacToe" -> {
                        actionName.text = root.context.getString(R.string.tic_tac_toe)
                        actionImage.setImageResource(R.drawable.ic_tic_tac_toe)
                    }
                }

                operationView.setOnClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        binding =
            AccountViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val info = dataSet[position]

        viewHolder.bind(info, onClick)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

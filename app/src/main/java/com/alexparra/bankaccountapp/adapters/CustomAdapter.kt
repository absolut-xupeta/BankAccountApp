package com.alexparra.bankaccountapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.TransactionViewBinding
import com.alexparra.bankaccountapp.objects.AccountsManager.formatMoneyTransaction

class CustomAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var binding: TransactionViewBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        binding = TransactionViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding.root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // [0] transferType | [1] name | [2] amount | [3] date
        val info = dataSet[position].split(';')

        val transformedValue = formatMoneyTransaction(info[2].toLong())

        with(binding) {
            if (info[0] == "Sent") {
                transferImage.setImageResource(R.drawable.ic_transfer_send)
            } else {
                transferImage.setImageResource(R.drawable.ic_transfer_receive)
            }

            transferType.text = info[0]

            transferDate.text = info[3]

            name.text = info[1]

            amount.text = transformedValue
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
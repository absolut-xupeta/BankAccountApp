package com.alexparra.bankaccountapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.objects.AccountsManager.formatMoneyTransaction

class CustomAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val transferImage: ImageView
        val transferType: TextView
        val transferDate: TextView
        val name: TextView
        val amount: TextView

        init {
            // Define click listener for the ViewHolder's View.
            transferImage = view.findViewById(R.id.transferImage)
            transferType = view.findViewById(R.id.transferType)
            transferDate = view.findViewById(R.id.transferDate)
            name = view.findViewById(R.id.name)
            amount = view.findViewById(R.id.amount)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.transaction_view, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // [0] transferType | [1] name | [2] amount | [3] date
        val info = dataSet[position].split(';')

        val transformedValue = formatMoneyTransaction(info[2].toLong())

        if (info[0] == "Sent") {
            viewHolder.transferImage.setImageResource(R.drawable.ic_transfer_send)
        } else {
            viewHolder.transferImage.setImageResource(R.drawable.ic_transfer_receive)
        }

        viewHolder.transferType.text = info[0]

        viewHolder.transferDate.text = info[3]

        viewHolder.name.text = info[1]

        viewHolder.amount.text = transformedValue.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
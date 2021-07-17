package com.alexparra.bankaccountapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.AccountViewBinding
import com.alexparra.bankaccountapp.model.Services

class ServicesAdapter(
    private val dataSet: ArrayList<Services>,
    private val onClick: (Services) -> Unit
) :
    RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private lateinit var binding: AccountViewBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val binding: AccountViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Services) {
            with(binding) {

                actionName.text = service.text
                actionImage.setImageResource(service.image)

                if (service.soon) {
                    operationView.isClickable = false
                    operationView.isFocusable = false
                    warning.visibility = View.VISIBLE
                    warning.text = root.context.getString(R.string.soon_warning)
                }

                operationView.setOnClickListener {
                    onClick.invoke(service)
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

        viewHolder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}

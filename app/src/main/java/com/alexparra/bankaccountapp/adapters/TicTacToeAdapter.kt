package com.alexparra.bankaccountapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.databinding.TicTacToeViewBinding

class TicTacToeAdapter(
    private val dataSet: Array<Int>,
    private val onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<TicTacToeAdapter.ViewHolder>() {

    private lateinit var binding: TicTacToeViewBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val binding: TicTacToeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(id: Int, onClick: (Int) -> Unit) {
            val symbolX = "X"
            val symbolO = "O"

            with(binding) {
                cellText.setOnClickListener {

                    if (isBotRound) {
                        cellText.text = symbolO

                        addToMatrix(id, symbolO)
                        counter += 1

                        isBotRound = false
                        cellText.isClickable = false

                        onClick.invoke(id)
                    } else {
                        cellText.text = symbolX


                        addToMatrix(id, symbolX)
                        counter += 1

                        isBotRound = true
                        cellText.isClickable = false

                        onClick.invoke(id)
                    }
                }
            }
        }

        private fun addToMatrix(id: Int, symbol: String) {
            when (id) {
                in 1..3 -> {
                    matrix[0][id-1] = symbol
                }
                in 4..6 -> {
                    matrix[1][id-4] = symbol
                }
                else -> {
                    matrix[2][id-7] = symbol
                }
            }
        }
    }

    companion object {
        var matrix: Array<Array<String>> = arrayOf(Array(3) {""}, Array(3) {""},Array(3) {""})

        var isBotRound = false

        var counter = 1

        // TODO GET BEST USE FOR THIS FUNCTION
        //fun getTicTacToeMatrix() = matrix
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding =
            TicTacToeViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val row = dataSet[position]

        viewHolder.bind(row, onClick)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
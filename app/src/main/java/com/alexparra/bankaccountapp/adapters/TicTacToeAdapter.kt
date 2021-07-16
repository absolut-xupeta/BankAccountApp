package com.alexparra.bankaccountapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.databinding.TicTacToeViewBinding
import com.alexparra.bankaccountapp.objects.TicTacToeManager
import com.alexparra.bankaccountapp.objects.TicTacToeManager.board
import com.alexparra.bankaccountapp.objects.TicTacToeManager.matrix

class TicTacToeAdapter(
    private var dataSet: Array<CellState>,
    private val onClick: (CellState, Int) -> Unit
) :
    RecyclerView.Adapter<TicTacToeAdapter.ViewHolder>() {

    private lateinit var binding: TicTacToeViewBinding
    private var clickable: Boolean = true

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(private val binding: TicTacToeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: CellState, position: Int) {
            with(binding) {

                cellText.text = ""

                cellText.setOnClickListener {
                    if (!clickable || state != CellState.NONE) {
                        return@setOnClickListener
                    }

                    onClick.invoke(state, position)

                    // TODO remove bellow

//                    if (isBotRound) {
//                        cellText.text = symbolO
//
//                        addToMatrix(id, symbolO)
//                        roundCount += 1
//
//                        isBotRound = false
//                        cellText.isClickable = false
//
//                        onClick.invoke(id)
//                    } else {
//                        cellText.text = symbolX
//
//
//                        addToMatrix(id, symbolX)
//                        roundCount += 1
//
//                        isBotRound = true
//                        cellText.isClickable = false
//
//                        onClick.invoke(id)
//                    }
                }
            }
        }
    }
//  0, 1, 2,
//  3, 4, 5
//  6, 7, 8

    fun markCell(pos: Int, state: CellState) {
        dataSet[pos] = state
        notifyDataSetChanged() // TODO check notifyItemChanged
    }

    fun reset() {
        dataSet = board
        notifyDataSetChanged()
        TicTacToeManager.resetAll()
        clickable = true
    }

    fun disableBoard() {
        clickable = false
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        binding =
            TicTacToeViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    enum class CellState(val value: Char) {
        X('X'), O('O'), NONE(' ')
    }
}
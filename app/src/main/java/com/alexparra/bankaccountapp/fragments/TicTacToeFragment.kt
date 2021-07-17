package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.adapters.TicTacToeAdapter
import com.alexparra.bankaccountapp.databinding.FragmentTicTacToeBinding
import com.alexparra.bankaccountapp.objects.TicTacToeManager.board
import com.alexparra.bankaccountapp.objects.TicTacToeManager.isBotRound
import com.alexparra.bankaccountapp.objects.TicTacToeManager.roundCount
import com.google.android.material.snackbar.Snackbar

class TicTacToeFragment : Fragment() {

    private lateinit var binding: FragmentTicTacToeBinding

    private lateinit var ticTacToeAdapter: TicTacToeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicTacToeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticTacToe()
    }

    private fun ticTacToe() {
        ticTacToeAdapter.reset()
        resetViews()

        val recyclerView: RecyclerView = binding.recyclerView

        ticTacToeAdapter = TicTacToeAdapter(board, ::onInteraction)

        recyclerView.apply {
            adapter = ticTacToeAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

    }

    private fun onInteraction(state: TicTacToeAdapter.CellState, pos: Int) {
        ticTacToeAdapter.isBoardEnabled(true)

        ticTacToeAdapter.apply {
            val marker =
                if (isBotRound) TicTacToeAdapter.CellState.O else TicTacToeAdapter.CellState.X

            markCell(pos, marker)

            updateScreen(marker)

            isBotRound = !isBotRound

            if(roundCount > 5) checkResult()
        }
    }

    private fun checkResult() {
        when (checkStatus()) {
            "X" -> {
                ticTacToeAdapter.isBoardEnabled(false)
                Snackbar.make(view as View, getString(R.string.x_wins), Snackbar.LENGTH_LONG)
                    .setAction("Retry") { ticTacToeAdapter.reset() }.show()
                resetViews()

            }

            "O" -> {
                ticTacToeAdapter.isBoardEnabled(false)
                resetViews()
                Toast.makeText(context, getString(R.string.o_wins), Toast.LENGTH_LONG).show()
            }

            "Draw" -> {
                ticTacToeAdapter.isBoardEnabled(false)
                resetViews()
                Toast.makeText(context, getString(R.string.draw), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkStatus(): String {
        val matrix = matrix
        var result = "none"

        // TODO CHANGE TO BOARD
        // Check row
        outer@ for (i in matrix) {
            inner@ for (j in i.indices) {
                if (i[j].isBlank()) {
                    continue@outer
                } else {
                    if (i.size <= j + 2) {
                        continue@outer
                    } else {
                        if (i[j] == i[j + 1] && i[j] == i[j + 2]) {
                            result = i[j]
                            return result
                        } else {
                            continue@inner
                        }
                    }
                }
            }
        }

        // Check column
        val pos = 0
        column@ for (j in 0..2) {
            if (matrix[pos][j].isBlank()) {
                continue@column
            } else {
                if (matrix[pos][j] == matrix[pos + 1][j] && matrix[pos][j] == matrix[pos + 2][j]) {
                    result = matrix[pos][j]
                    return result
                } else {
                    continue@column
                }
            }
        }

        // Check Transversal
        if (matrix[pos][pos].isNotBlank() && matrix[pos + 2][pos + 2].isNotBlank()) {
            if (matrix[pos][pos] == matrix[pos + 1][pos + 1] && matrix[pos][pos] == matrix[pos + 2][pos + 2]) {
                result = matrix[pos][pos]
                return result
            }

            if (matrix[pos][pos + 2] == matrix[pos + 1][pos + 1] && matrix[pos][pos + 2] == matrix[pos + 2][pos]) {
                result = matrix[pos][pos]
                return result
            }
        }

        return if (roundCount == 10) "Draw" else result
    }


    private fun updateScreen(marker: TicTacToeAdapter.CellState) {
        with(binding) {
            counter.text = roundCount.toString()

            if (marker == TicTacToeAdapter.CellState.O) {
                roundButton.text = getString(R.string.player2_round)
                roundButton.setTextColor(
                    getColor(
                        activity as AppCompatActivity,
                        R.color.player2
                    )
                )
            } else {
                roundButton.text = getString(R.string.player1_round)
                roundButton.setTextColor(
                    getColor(
                        activity as AppCompatActivity,
                        R.color.player1
                    )
                )
            }
        }
    }

    private fun resetViews() {
        with(binding) {
            counter.text = "1"
            roundButton.text = getString(R.string.player1_round)
            roundButton.setTextColor(
                getColor(
                    activity as AppCompatActivity,
                    R.color.player1
                )
            )
        }
    }
}
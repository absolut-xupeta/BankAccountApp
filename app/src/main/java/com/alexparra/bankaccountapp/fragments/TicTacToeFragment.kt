package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.adapters.TicTacToeAdapter
import com.alexparra.bankaccountapp.databinding.FragmentTicTacToeBinding
import com.alexparra.bankaccountapp.objects.TicTacToeManager
import com.alexparra.bankaccountapp.objects.TicTacToeManager.board
import com.alexparra.bankaccountapp.objects.TicTacToeManager.checkStatus
import com.alexparra.bankaccountapp.objects.TicTacToeManager.isBotRound
import com.alexparra.bankaccountapp.objects.TicTacToeManager.roundCount
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

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
        val recyclerView: RecyclerView = binding.recyclerView

        ticTacToeAdapter = TicTacToeAdapter(board, ::onInteraction)

        ticTacToeAdapter.reset()
        resetViews()

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
            roundCount += 1

            updateScreen(marker)

            isBotRound = !isBotRound

            if (roundCount > 5) {
                when (checkStatus()) {
                    TicTacToeManager.GameStatus.X_WON -> finishGame(TicTacToeManager.GameStatus.X_WON)

                    TicTacToeManager.GameStatus.O_WON -> finishGame(TicTacToeManager.GameStatus.O_WON)

                    TicTacToeManager.GameStatus.DRAW -> finishGame(TicTacToeManager.GameStatus.DRAW)

                    TicTacToeManager.GameStatus.IN_PROGRESS -> ticTacToeAdapter.isBoardEnabled(true)
                }
            }
        }
    }

    private fun finishGame(winner: TicTacToeManager.GameStatus) {
        val result = when(winner) {
            TicTacToeManager.GameStatus.X_WON -> getString(R.string.x_wins)
            TicTacToeManager.GameStatus.O_WON -> getString(R.string.o_wins)
            TicTacToeManager.GameStatus.DRAW -> getString(R.string.draw)
            else -> throw Exception("The function needs one of the above values.")
        }

        ticTacToeAdapter.isBoardEnabled(false)
        Snackbar.make(view as View, result, Snackbar.LENGTH_LONG)
            .setAction("Retry") { ticTacToeAdapter.reset() }.show()
        resetViews()
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
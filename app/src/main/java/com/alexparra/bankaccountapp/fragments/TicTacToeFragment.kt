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

class TicTacToeFragment : Fragment() {

    private lateinit var binding: FragmentTicTacToeBinding

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
        TicTacToeAdapter.isBotRound = false
        TicTacToeAdapter.counter = 1

        val board = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        with(binding) {
            roundButton.text = getString(R.string.player1_round)
            roundButton.setTextColor(
                getColor(
                    activity as AppCompatActivity,
                    R.color.player1
                )
            )

            counter.text = TicTacToeAdapter.counter.toString()

            val recyclerView: RecyclerView = recyclerView


            val ticTacToeAdapter = TicTacToeAdapter(board) {
                counter.text = TicTacToeAdapter.counter.toString()

                if (!TicTacToeAdapter.isBotRound) {

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

                if (TicTacToeAdapter.counter > 5) {
                    when(checkStatus()) {
                        "X" -> Toast.makeText(context, "X WINS", Toast.LENGTH_LONG).show()
                        "O" -> Toast.makeText(context, "X WINS", Toast.LENGTH_LONG).show()
                        else -> Toast.makeText(context, "DRAW", Toast.LENGTH_LONG).show()
                    }
                }
            }



            recyclerView.apply {
                adapter = ticTacToeAdapter
                layoutManager = GridLayoutManager(context, 3)
            }

            // TODO MOVE THIS TO THE RESET VIEW FUNC
            if (TicTacToeAdapter.counter == 9) TicTacToeAdapter.counter = 0
        }
    }

    private fun checkStatus(): String {
        val matrix = TicTacToeAdapter.matrix
        var result = "none"

        // Check row
        outer@ for (i in matrix) {
            inner@ for (j in i.indices) {
                if (i[j].isBlank()) {
                    continue@outer
                } else {
                    if (i.size < j+2) {
                        continue@outer
                    } else {
                        if (i[j] == i[j+1] && i[j] == i[j+2]) {
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

        // Check Transversal

        return result
    }
}
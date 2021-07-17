package com.alexparra.bankaccountapp.objects

import com.alexparra.bankaccountapp.adapters.TicTacToeAdapter

object TicTacToeManager {
    var board = Array(9) {
        TicTacToeAdapter.CellState.NONE
    }

    var isBotRound = false

    var roundCount = 1

    fun resetAll() {
        board = Array(9) {
            TicTacToeAdapter.CellState.NONE
        }
        isBotRound = false
        roundCount = 1
    }
}
package com.alexparra.bankaccountapp.objects

import com.alexparra.bankaccountapp.adapters.TicTacToeAdapter

object TicTacToeManager {
    var matrix =
        arrayOf(CharArray(3) {' '}, CharArray(3) {' '}, CharArray(3) {' '})

    var board = Array(9) {
        TicTacToeAdapter.CellState.NONE
    }

    var isBotRound = false

    var gameEnd = false

    var roundCount = 1

    fun resetAll() {
        matrix = arrayOf(CharArray(3) {' '}, CharArray(3) {' '}, CharArray(3) {' '})
        isBotRound = false
        gameEnd = false
        roundCount = 1
    }

    fun addToMatrix(id: Int, symbol: TicTacToeAdapter.CellState) {
        when (id) {
            in 1..3 -> {
                matrix[0][id - 1] = symbol.value
            }
            in 4..6 -> {
                matrix[1][id - 4] = symbol.value
            }
            else -> {
                matrix[2][id - 7] = symbol.value
            }
        }
    }
}
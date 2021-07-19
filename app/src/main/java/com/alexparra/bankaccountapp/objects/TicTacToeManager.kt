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

    fun checkStatus(): GameStatus {
        val board = board

        // Check row
        for (i in board.indices step 3) {
            if (board[i] == TicTacToeAdapter.CellState.NONE) {
                continue
            } else {
                if (board[i] == board[i + 1] && board[i] == board[i + 2]) {
                    return if (board[i] == TicTacToeAdapter.CellState.X) GameStatus.X_WON else GameStatus.O_WON
                } else {
                    continue
                }
            }
        }

        // Check column
        for (i in 0..2) {
            if (board[i] == TicTacToeAdapter.CellState.NONE) {
                continue
            } else {
                if (board[i] == board[i + 3] && board[i] == board[i + 6]) {
                    return if (board[i] == TicTacToeAdapter.CellState.X) GameStatus.X_WON else GameStatus.O_WON
                } else {
                    continue
                }
            }
        }

        // Check Transversal
        if (board[0] != TicTacToeAdapter.CellState.NONE) {
            if (board[0] == board[4] && board[0] == board[8]) {
                return if (board[0] == TicTacToeAdapter.CellState.X) GameStatus.X_WON else GameStatus.O_WON
            }
        }

        if (board[2] != TicTacToeAdapter.CellState.NONE) {
            if (board[2] == board[4] && board[2] == board[6]) {
                return if (board[2] == TicTacToeAdapter.CellState.X) GameStatus.X_WON else GameStatus.O_WON
            }
        }

        return if (roundCount == 10) GameStatus.DRAW else  GameStatus.IN_PROGRESS
    }

    enum class GameStatus {
        X_WON, O_WON, DRAW, IN_PROGRESS
    }
}
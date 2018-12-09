package com.github.mirasrael.tictactoe

/**
 * Date: 12/8/2018
 * Time: 11:05
 */
class Game(val colCount: Int = 4, val rowCount: Int = 4, val winningCount: Int = 3) {
    private val field: Array<Sign?> = arrayOfNulls(colCount * rowCount)
    private val player1: Player = Player(TIC, "Player 1")
    private val player2: Player = Player(TAC, "Player 2")
    private var winner: Player? = null
    private var turn: Turn

    init {
        turn = Turn(player1, 1)
    }

    fun makeTurn(position: Position): Boolean {
        if (winner != null) {
            return false
        }
        println("${turn.player.name} tries to make turn at $position")
        if (!tryPlaceSign(position))
            return false
        println("${turn.player.name} made turn at $position")
        if (checkWinCondition(position)) {
            winner = turn.player
            println("${winner?.name} win!!!!")
            return true
        }
        turn.player = if (turn.player === player1) player2 else player1
        turn.num++
        println("Turn number ${turn.num}, ${turn.player.name} [${turn.player.sign.symbol}]")
        return true
    }

    fun forEachSign(action: (sign: Sign, position: Position) -> Unit) {
        field.forEachIndexed { index, it ->
            if (it != null) {
                action(it, Position(index / colCount, index % colCount))
            }
        }
    }

    /*
    0 [?, ?, ?, ?]
    1 [?, ?, ?, ?]
    2 [?, ?, ?, ?]
    3 [?, ?, ?, ?]
     */
    private fun checkWinCondition(position: Position): Boolean {
        val sign = turn.player.sign
        // check column
        for (start in (Math.max(0, position.row - (winningCount - 1))..position.row)) {
            if (start + winningCount > rowCount) {
                continue
            }
            if ((start until start + winningCount).all { field[toIndex(it, position.col)] == sign })
            {
                return true
            }
        }
        // check row
        for (start in (Math.max(0, position.col - (winningCount - 1))..position.col)) {
            if (start + winningCount - 1 > colCount) {
                continue
            }
            if ((start until start + winningCount).all { field[toIndex(position.row, it)] == sign })
            {
                return true
            }
        }
        return false
    }

    private fun toIndex(row: Int, col: Int): Int = row * colCount + col

    private fun tryPlaceSign(position: Position): Boolean {
        val index = toIndex(position.row, position.col)
        if (field[index] != null) {
            return false
        }
        field[index] = turn.player.sign
        return true
    }
}
package com.github.mirasrael.tictactoe

/**
 * Date: 12/8/2018
 * Time: 11:05
 */
class Game(val colCount: Int = 4, val rowCount: Int = 4, val winningCount: Int = 3) {
    private val field: Array<Sign?> = arrayOfNulls(colCount * rowCount)
    private val player1: Player = Player(TIC, "Player 1")
    private val player2: Player = Player(TAC, "Player 2")
    private var turn: Turn

    var winner: Player? = null

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

    private fun checkWinCondition(position: Position): Boolean {
        val sign = turn.player.sign
        val maxOffset = winningCount - 1

        fun checkRange(deltaRow: Int, deltaCol: Int): Boolean {
            for (offset in 0..maxOffset) {
                val row = position.row - offset * deltaRow
                val col = position.col - offset * deltaCol
                val minRow = row + (if (deltaRow < 0) deltaRow * maxOffset else 0)
                val maxRow = row + (if (deltaRow > 0) deltaRow * maxOffset else 0)
                val minCol = col + (if (deltaCol < 0) deltaCol * maxOffset else 0)
                val maxCol = col + (if (deltaCol > 0) deltaCol * maxOffset else 0)
                if (minRow < 0 || minCol < 0 || maxRow >= rowCount || maxCol >= colCount)
                {
                    continue
                }
                if ((0..maxOffset).all { field[toIndex(row + deltaRow * it, col + deltaCol * it)] == sign })
                {
                    return true
                }
            }
            return false
        }

        return checkRange(0, 1) || checkRange(1, 0) || checkRange(1, 1) || checkRange(1, -1)
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
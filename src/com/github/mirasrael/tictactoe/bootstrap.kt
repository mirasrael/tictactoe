package com.github.mirasrael.tictactoe

import java.awt.Dimension
import javax.swing.JFrame

/**
 * Date: 12/8/2018
 * Time: 11:10
 */
fun createMainWindow(width: Int = 600, height: Int = 600) {
    val frame = JFrame("Tic tac toe")

    val game = Game()
    val gameCanvas = GameCanvas(game)
    gameCanvas.preferredSize = Dimension(width, height)
    frame.add(gameCanvas)
    frame.pack()
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}
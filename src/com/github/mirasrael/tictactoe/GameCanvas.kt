package com.github.mirasrael.tictactoe

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class GameCanvas(val game: Game) : JPanel() {
    class MouseEvents(private val gameCanvas: GameCanvas) : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.button == MouseEvent.BUTTON1) {
                this.gameCanvas.onLeftMouseButtonClicked(e)
            }
        }
    }

    init {
        addMouseListener(MouseEvents(this))
    }

    private val colWidth get() = this.width / game.colCount
    private val rowHeight get() = this.height / game.rowCount

    override fun paintComponent(g: Graphics) {
        drawField(g)
        if (game.winner != null) {
            showWinner(g)
        }
    }

    private fun showWinner(g: Graphics) {
        g.font = Font("Arial", Font.BOLD, 32)
        val winner = game.winner ?: throw NullPointerException()
        val displayText = "${winner.name} [${winner.sign.symbol}] win!!!"
        val fontMetrics = g.fontMetrics
        val textWidth = fontMetrics.stringWidth(displayText)
        val textHeight = fontMetrics.height
        val x = (this.width - textWidth) / 2
        val y = (this.height - textHeight) / 2 - textHeight
        g.color = Color.WHITE
        g.fillRect(x, y, textWidth, textHeight)
        g.color = Color.BLACK
        g.drawString(displayText, x, y + textHeight)
    }

    private fun drawField(g: Graphics) {
        val rowHeight = this.rowHeight
        val colWidth = this.colWidth

        for (row in 1 until game.rowCount) {
            val y = row * rowHeight
            g.drawLine(0, y, this.width, y)
        }

        for (col in 1 until game.colCount) {
            val x = col * colWidth
            g.drawLine(x, 0, x, this.height)
        }

        game.forEachSign { sign, position -> drawSign(g, sign, position) }
    }

    private fun drawSign(g: Graphics, sign: Sign, position: Position) {
        val x = position.col * colWidth
        val y = position.row * rowHeight
        if (sign == TIC) {
            drawTic(g, x, y)
        } else {
            drawTac(g, x, y)
        }
    }

    private fun drawTic(g: Graphics, x: Int, y: Int) {
        val x2 = x + colWidth
        val y2 = y + rowHeight
        g.drawLine(x, y, x2, y2)
        g.drawLine(x, y2, x2, y)
    }

    private fun drawTac(g: Graphics, x: Int, y: Int) {
        g.drawOval(x, y, colWidth, rowHeight)
    }

    private fun onLeftMouseButtonClicked(e: MouseEvent) {
        val row = e.y / rowHeight
        val col = e.x / colWidth
        if (game.makeTurn(Position(row, col))) {
            repaint()
        }
    }
}
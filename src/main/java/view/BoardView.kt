package view

import com.codingame.game.Player
import com.codingame.gameengine.module.entities.*
import domainExpansion.Board
import domainExpansion.Cell
import domainExpansion.Constant

class BoardView(private val board: Board, private val graphics: GraphicEntityModule) {
    private val grid = graphics.createGroup()
    private val group: Group = graphics.createGroup(grid)

    init {
        board.setView(this);

        group.setX(Constant.LEFT_PAD).setY(Constant.TOP_PAD);

        drawBackground();
        drawGrid();
    }

    private fun drawBackground() {
        graphics
            .createRectangle()
            .setWidth(1920)
            .setHeight(1080)
            .setFillColor(0x7e7e81)
            .setZIndex(-5);
    }

    private fun drawGrid() {
        for (y in 0..Constant.HEIGHT) {
            val vertical: Line = graphics.createLine()
                .setX(0)
                .setY(y * Constant.CELL_SIZE)
                .setX2(Constant.WIDTH * Constant.CELL_SIZE)
                .setY2(y * Constant.CELL_SIZE)
                .setLineColor(0x000000)
                .setLineWidth(5.0)
                .setZIndex(-1);
            grid.add(vertical);
        }

        for (x in 0 .. Constant.WIDTH ) {
            val horizontal: Line = graphics.createLine()
                .setX(x * Constant.CELL_SIZE)
                .setY(0)
                .setX2(x * Constant.CELL_SIZE)
                .setY2(Constant.HEIGHT * Constant.CELL_SIZE)
                .setLineColor(0x000000)
                .setLineWidth(5.0)
                .setZIndex(-1);
            grid.add(horizontal);
        }
    }

    fun addPlayer(player: Player) {
        group.add(SovereignView(player.sovereign, graphics, player.colorToken).group)
    }

    fun addWall(cellA: Cell, cellB: Cell) {
        // TODO
    }
}
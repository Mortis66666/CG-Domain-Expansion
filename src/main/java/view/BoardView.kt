package view

import com.codingame.game.Player
import com.codingame.gameengine.module.entities.*
import domainExpansion.Board
import domainExpansion.Cell
import domainExpansion.Constant

class BoardView(private val board: Board, private val graphics: GraphicEntityModule) {
    private val grid = graphics.createGroup()
    private val players = graphics.createGroup()
    private val walls = graphics.createGroup()
    private val group: Group = graphics.createGroup(players, grid, walls)

    init {
        board.setView(this);

        group.setX(Constant.LEFT_PAD).setY(Constant.TOP_PAD);
        grid.zIndex = -1
        players.zIndex = 2

        drawBackground();
        drawGrid();
        drawIndex();
    }

    private fun drawBackground() {
        graphics
            .createRectangle()
            .setWidth(Constant.SCREEN_WIDTH)
            .setHeight(Constant.SCREEN_HEIGHT)
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

            grid.add(horizontal);
        }
    }

    private fun drawIndex() {
        for (y in 0 until Constant.HEIGHT) {
            val text: Text = graphics.createText()
                .setText(y.toString())
                .setX(-Constant.TOP_PAD)
                .setY(((y + .5) * Constant.CELL_SIZE).toInt())
                .setFontSize(Constant.TOP_PAD)
                .setFillColor(0x000000)

            grid.add(text);
        }

        for (x in 0 until Constant.WIDTH) {
            val text: Text = graphics.createText()
                .setText(x.toString())
                .setX(((x + .5) * Constant.CELL_SIZE).toInt())
                .setY(-Constant.TOP_PAD)
                .setFontSize(Constant.TOP_PAD)
                .setFillColor(0x000000)

            grid.add(text);
        }
    }

    fun addPlayer(player: Player) {
        players.add(SovereignView(player.sovereign, graphics, player.colorToken).group)
        PlayerView(player, graphics)
    }

    fun addWall(cell: Cell, vertical: Boolean, colorToken: Int) {
        if (vertical) {
            val wall: Line = graphics.createLine()
                .setX(cell.x * Constant.CELL_SIZE)
                .setY(cell.y * Constant.CELL_SIZE)
                .setX2(cell.x * Constant.CELL_SIZE)
                .setY2((cell.y + 1) * Constant.CELL_SIZE)
                .setLineColor(colorToken)
                .setLineWidth(12.0)

            walls.add(wall);
        } else {
            val wall: Line = graphics.createLine()
                .setX(cell.x * Constant.CELL_SIZE)
                .setY(cell.y * Constant.CELL_SIZE)
                .setX2((cell.x + 1) * Constant.CELL_SIZE)
                .setY2(cell.y * Constant.CELL_SIZE)
                .setLineColor(colorToken)
                .setLineWidth(12.0)

            walls.add(wall);
        }
    }
}
package com.codingame.game

import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import domainExpansion.Board
import domainExpansion.Constant
import view.BoardView

typealias Point = Pair<Int, Int>

class Referee : AbstractReferee() {
    @Inject
    private val gameManager: MultiplayerGameManager<Player>? = null

    @Inject
    private val graphicEntityModule: GraphicEntityModule? = null

    private lateinit var board: Board

    override fun init() {
        board = Board();
        if (graphicEntityModule != null) {
            BoardView(board, graphicEntityModule)
        }

        board.setup(gameManager!!.getPlayer(0), Point(0, 0))
        board.setup(gameManager.getPlayer(1), Point(Constant.WIDTH - 1, Constant.HEIGHT - 1))
    }

    override fun gameTurn(turn: Int) {
        val player = gameManager!!.getPlayer(1 - (turn % 2))

        player.sendInputLine("input")
        player.execute()

        try {
            val outputs = player.outputs
            // Check validity of the player output and compute the new game state

            board.movePlayer(player, Point(0, 3))
        } catch (e: AbstractPlayer.TimeoutException) {
            player.deactivate(String.format("$%d timeout!", player.index))
        }
    }
}
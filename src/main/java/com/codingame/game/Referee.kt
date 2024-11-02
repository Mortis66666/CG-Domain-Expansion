package com.codingame.game

import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.codingame.gameengine.module.endscreen.EndScreenModule
import com.google.inject.Inject
import domainExpansion.Board
import domainExpansion.Constant
import domainExpansion.InvalidAction
import view.BoardView

typealias Point = Pair<Int, Int>

class Referee : AbstractReferee() {
    @Inject
    private val gameManager: MultiplayerGameManager<Player>? = null

    @Inject
    private val graphicEntityModule: GraphicEntityModule? = null

    @Inject
    private val endScreenModule: EndScreenModule? = null

    private lateinit var board: Board

    override fun init() {
        board = Board();
        if (graphicEntityModule != null) {
            BoardView(board, graphicEntityModule)
        }

        board.setup(gameManager!!.getPlayer(0), Point(0, 0))
        board.setup(gameManager.getPlayer(1), Point(Constant.WIDTH - 1, Constant.HEIGHT - 1))

        gameManager.apply {
            firstTurnMaxTime = Constant.FIRST_TURN_MAX_TIME
            turnMaxTime = Constant.TURN_MAX_TIME
            maxTurns = Constant.MAX_TURNS
        }
    }

    override fun gameTurn(turn: Int) {
        val player = gameManager!!.getPlayer(1 - (turn % 2))
        val opponent = gameManager.getPlayer(turn % 2)

        if (turn <= 2) {
            player.sendInputLine(Constant.WIDTH.toString() + " " + Constant.HEIGHT)
        }

        player.sendInputLine(player.sovereign.toString())
        player.sendInputLine(opponent.sovereign.toString())

        player.sendInputLine(board.walls.size.toString())
        for (wall in board.walls) {
            player.sendInputLine(wall.toString())
        }

        player.execute()

        try {
            val action = player.action
            board.doAction(player, action)

            System.err.println("Player ${player.index} moved to ${action.x}, ${action.y}, placing wall ${action.direction}");

            val playerScore = board.countDomain(player, opponent)

            if (playerScore > 0) {
                val opponentScore = board.countDomain(opponent, player)

                if (playerScore > opponentScore) {
                    gameManager.addToGameSummary(String.format("Player %d won by conquering %d cells!", player.index, playerScore))
                } else if (playerScore < opponentScore) {
                    gameManager.addToGameSummary(String.format("Player %d won by conquering %d cells!", opponent.index, opponentScore))
                } else {
                    gameManager.addToGameSummary("It's a tie!")
                }

                player.score = playerScore
                opponent.score = opponentScore
                gameManager.endGame()
            }
        } catch (e: AbstractPlayer.TimeoutException) {
            player.score = -7
            player.deactivate(String.format("$%d timeout!", player.index))
            gameManager.endGame()
        } catch (e: InvalidAction) {
            player.score = -6
            player.deactivate("Invalid action!")
            gameManager.endGame()
        }
    }

    override fun onEnd() {
        val scores = gameManager!!.players.stream().mapToInt { p: Player -> p.score }.toArray()
        val texts = arrayOfNulls<String>(2)
        for (i in scores.indices) {
            if (scores[i] == -7) {
                texts[i] = "Time out!"
                continue
            } else if (scores[i] == -6) {
                texts[i] = "Invalid action!"
                continue
            }
            texts[i] = "Conquered ${scores[i]} cells."
        }
        endScreenModule!!.setScores(scores, texts)
    }
}
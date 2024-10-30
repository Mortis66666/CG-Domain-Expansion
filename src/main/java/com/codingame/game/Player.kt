package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Text
import domainExpansion.Action
import domainExpansion.Action.Direction
import domainExpansion.InvalidAction
import domainExpansion.Sovereign

// Uncomment the line below and comment the line under it to create a Solo Game
// class Player : AbstractSoloPlayer() {
class Player : AbstractMultiplayerPlayer() {
    lateinit var sovereign: Sovereign
    var message: Text? = null

    override fun getExpectedOutputLines(): Int = 1

    @get:Throws(TimeoutException::class, InvalidAction::class)
    val action: Action
        get() = try {
            val playerAction = outputs[0]

            val match = PLAYER_ACTION_REGEX.matchEntire(playerAction)
                ?: throw InvalidAction("Invalid output.")

            val x = match.groups["x"]?.value?.toInt() ?: throw InvalidAction("Invalid x coordinate.")
            val y = match.groups["y"]?.value?.toInt() ?: throw InvalidAction("Invalid y coordinate.")
            val direction = when (match.groups["direction"]?.value) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> Direction.UP
            }

            val msg = match.groups["message"]?.value
            message?.text = when {
                msg.isNullOrEmpty() -> " "
                msg.length < 20 -> msg
                else -> msg.take(17) + "..."
            }

            Action(x, y, direction)
        } catch (e: TimeoutException) {
            throw e
        } catch (e: InvalidAction) {
            throw e
        } catch (e: Exception) {
            throw InvalidAction("Invalid output.")
        }

    companion object {
        private val PLAYER_ACTION_REGEX = Regex("(?i)(?<x>[0-6])\\s+(?<y>[0-6])\\s+(?<direction>[A-Za-z]+)\\s*(?<message>.*)?")
    }
}

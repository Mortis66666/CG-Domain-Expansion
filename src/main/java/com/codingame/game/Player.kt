package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Text
import domainExpansion.Action
import domainExpansion.Action.Direction
import domainExpansion.InvalidAction
import domainExpansion.Sovereign
import java.util.regex.Matcher
import java.util.regex.Pattern

// Uncomment the line below and comment the line under it to create a Solo Game
// public class Player extends AbstractSoloPlayer {
class Player : AbstractMultiplayerPlayer() {
    lateinit var sovereign: Sovereign

    var message: Text? = null
    override fun getExpectedOutputLines(): Int {
        return 1
    }

    @get:Throws(TimeoutException::class, InvalidAction::class)
    val action: Action
        get() = try {
            val playerAction = outputs[0]
            val match: Matcher = PLAYER_ACTION_PATTERN.matcher(playerAction)
            if (match.matches()) {
                val msg = match.group("message")
                if (msg == null) message!!.text = " " else if (msg.length < 20) message!!.text =
                    msg else message!!.text = msg.substring(0, 17) + "..."
                val direction: Direction = when (match.group("direction")) {
                    "U" -> Direction.UP
                    "D" -> Direction.DOWN
                    "L" -> Direction.LEFT
                    "R" -> Direction.RIGHT
                    else -> throw InvalidAction("Invalid direction.")
                }

                Action(match.group("x").toInt(), match.group("y").toInt(), direction)
            } else {
                throw InvalidAction("Invalid output.")
            }
        } catch (e: TimeoutException) {
            throw e
        } catch (e: InvalidAction) {
            throw e
        } catch (e: Exception) {
            throw InvalidAction("Invalid output.")
        }

    companion object {
        private val PLAYER_ACTION_PATTERN = Pattern
            .compile("(?<x>-?[0-6]{1})\\s+(?<y>-?[0-6]{1})?\\s+(?<direction>[A-Za-z]{1})\\s*(?<message>.+)?\n")
    }
}
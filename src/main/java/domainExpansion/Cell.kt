package domainExpansion

import domainExpansion.Constant
import kotlin.math.abs

class Cell(val x: Int, val y: Int) {
    private var entity: Sovereign? = null
    val point = Pair(x, y)
    var canLeft = true
    var canRight = true
    var canUp = true
    var canDown = true
    
    private fun isOccupied(): Boolean {
        return entity != null
    }

    fun occupy(entity: Sovereign) {
        this.entity = entity
    }

    fun getEntity(): Sovereign? {
        return entity
    }

    fun free() {
        entity = null
    }

    fun distance(other: Cell): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun canGo(other: Cell, bodyBlock: Boolean = true): Boolean {
        if (distance(other) != 1) {
            return false
        }

        if (other.isOccupied() && bodyBlock) {
            return false
        }

        if (other.x < x) {
            return canLeft && other.canRight
        }

        if (other.x > x) {
            return canRight && other.canLeft
        }

        if (other.y < y) {
            return canUp && other.canDown
        }

        if (other.y > y) {
            return canDown && other.canUp
        }

        System.err.println("Error: exception")
        return false
    }

    fun hasWall(direction: Action.Direction): Boolean {
        return when (direction) {
            Action.Direction.UP -> !canUp
            Action.Direction.DOWN -> !canDown
            Action.Direction.LEFT -> !canLeft
            Action.Direction.RIGHT -> !canRight
        }
    }

    fun getEmptyDirections(): List<Action.Direction> {
        val directions = mutableListOf<Action.Direction>()
        if (canUp) directions.add(Action.Direction.UP)
        if (canDown) directions.add(Action.Direction.DOWN)
        if (canLeft) directions.add(Action.Direction.LEFT)
        if (canRight) directions.add(Action.Direction.RIGHT)
        return directions
    }

    fun equals(other: Cell): Boolean {
        return x == other.x && y == other.y
    }

    override fun toString(): String {
        return "$x $y"
    }
}
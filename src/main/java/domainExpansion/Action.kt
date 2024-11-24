
package domainExpansion
class Action(var x: Int, var y: Int, var direction: Direction) {
    val point = Pair(x, y)
    enum class Direction {
        UP, DOWN, LEFT, RIGHT, EMPTY;

        fun next(): Action.Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
                else -> EMPTY
            }
        }

        fun toStr(): String {
            return when (this) {
                UP -> "U"
                RIGHT -> "R"
                DOWN -> "D"
                LEFT -> "L"
                else -> "_"
            }
        }
    }

    companion object {
        fun empty(): Action {
            return Action(-1, -1, Direction.EMPTY)
        }
    }

    override fun toString(): String {
        return "$x $y ${direction.toStr()}"
    }

    fun equals(action: Action): Boolean {
        return x == action.x && y == action.y && direction == action.direction
    }
}
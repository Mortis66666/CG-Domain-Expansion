
package domainExpansion
class Action(var x: Int, var y: Int, var direction: Direction) {
    val point = Pair(x, y)
    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun next(): Action.Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }
    }

    override fun toString(): String {
        return "$x $y ${direction.name[0]}"
    }

    fun equals(action: Action): Boolean {
        return x == action.x && y == action.y && direction == action.direction
    }
}
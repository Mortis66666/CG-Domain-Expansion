
package domainExpansion
class Action(var x: Int, var y: Int, var direction: Direction) {
    val point = Pair(x, y)
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }
}

package domainExpansion
class Action(var x: Int, var y: Int, var direction: Direction) {
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
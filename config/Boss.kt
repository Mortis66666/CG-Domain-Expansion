import java.util.*

const val GRID_SIZE = 7
const val MAX_DISTANCE = 3

fun main(args: Array<String>) {
    val `in`: java.util.Scanner = java.util.Scanner(java.lang.System.`in`)
    val width: Int = `in`.nextInt()
    val height: Int = `in`.nextInt()
    while (true) {
        val startX: Int = `in`.nextInt()
        val startY: Int = `in`.nextInt()
        val opponentX: Int = `in`.nextInt()
        val opponentY: Int = `in`.nextInt()
        val numWalls: Int = `in`.nextInt()

        // Track walls in a set of pairs for easy access
        val walls: MutableSet<String> = HashSet<String>()
        for (i in 0 until numWalls) {
            val ax: Int = `in`.nextInt()
            val ay: Int = `in`.nextInt()
            val bx: Int = `in`.nextInt()
            val by: Int = `in`.nextInt()
            walls.add(getWallKey(ax, ay, bx, by))
            walls.add(getWallKey(bx, by, ax, ay)) // Add both directions
        }

        // BFS initialization
        val queue: java.util.Queue<IntArray> = LinkedList<IntArray>()
        val visited = Array(GRID_SIZE) {
            BooleanArray(
                GRID_SIZE
            )
        }
        queue.add(intArrayOf(startX, startY, 0)) // {x, y, distance}
        visited[startX][startY] = true
        var targetCell = intArrayOf(startX, startY, 0, Int.MAX_VALUE) // {x, y, distance, distToOpponent}
        val directions = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))
        while (!queue.isEmpty()) {
            val current: IntArray = queue.poll()
            val x = current[0]
            val y = current[1]
            val dist = current[2]
            if (dist > MAX_DISTANCE) continue  // Skip cells beyond max distance

            // Calculate Manhattan distance to the opponent
            val distToOpponent: Int = java.lang.Math.abs(x - opponentX) + java.lang.Math.abs(y - opponentY)

            // Check if this cell is a better candidate:
            // - It must be within MAX_DISTANCE.
            // - Prefer cells that are further from the start.
            // - Among cells with the same distance, prefer those closer to the opponent.
            if (dist > targetCell[2] || dist == targetCell[2] && distToOpponent < targetCell[3]) {
                targetCell = intArrayOf(x, y, dist, distToOpponent)
            }

            // Explore neighbors
            for (dir in directions) {
                val nx = x + dir[0]
                val ny = y + dir[1]
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE &&
                    !visited[nx][ny] && !walls.contains(getWallKey(x, y, nx, ny))
                ) {
                    visited[nx][ny] = true
                    queue.add(intArrayOf(nx, ny, dist + 1))
                }
            }
        }

        // Output the target cell coordinates
        println(targetCell[0].toString() + " " + targetCell[1] + " " + randomMove())
    }
}

// Helper method to create a unique key for walls
fun getWallKey(ax: Int, ay: Int, bx: Int, by: Int): String {
    return "$ax,$ay-$bx,$by"
}

fun randomMove(): String {
    val random: java.util.Random = java.util.Random()
    val moves = arrayOf("U", "D", "L", "R")
    return moves[random.nextInt(moves.size)]
}

package domainExpansion

import com.codingame.game.Player
import view.BoardView
import java.util.*

class Board {
    private lateinit var view: BoardView;
    val walls = mutableListOf<Wall>();
    private val grid = Array(Constant.HEIGHT) { y -> Array(Constant.WIDTH) { x -> Cell(x, y)} }

    init {

    }
    
    fun setView(view: BoardView) {
        this.view = view;
    }

    fun setup(player: Player, position: Point) {
        player.sovereign = Sovereign(getCell(position));
        view.addPlayer(player);
    }

    fun doAction(player: Player, action: Action) {
        movePlayer(player, action.point);
        val playerPosition = player.sovereign.on.point;

        var wallDirection = action.direction;

        while (player.sovereign.on.hasWall(wallDirection)) {
            wallDirection = wallDirection.next();
        }

        val cellB = when (wallDirection) {
            Action.Direction.UP -> Point(playerPosition.first, playerPosition.second - 1)
            Action.Direction.DOWN -> Point(playerPosition.first, playerPosition.second + 1)
            Action.Direction.LEFT -> Point(playerPosition.first - 1, playerPosition.second)
            Action.Direction.RIGHT -> Point(playerPosition.first + 1, playerPosition.second)
        }

        addWall(player, playerPosition, cellB);

//        player.view.updateMessage((player.message ?: "") as String)
    }

    fun countDomain(player: Player, opponent: Player): Int {
        // Use flood fill to count the domain, terminate when reach the opponent's sovereign
        val visited = mutableSetOf<Cell>()
        val queue = LinkedList<Cell>()
        val start = player.sovereign.on

        visited.add(start)
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (current == opponent.sovereign.on) {
                return -1
            }

            for (neighbor in getNeighbor(current)) {
                if (neighbor in visited || !current.canGo(neighbor, false)) {
                    continue
                }

                visited.add(neighbor)
                queue.add(neighbor)
            }
        }

        return visited.size
    }

    private fun getCell(x: Int, y: Int): Cell {
        return grid[y][x];
    }

    private fun getCell(p: Point): Cell {
        if (p.first < 0 || p.first >= Constant.WIDTH || p.second < 0 || p.second >= Constant.HEIGHT) {
            return Cell(p.first, p.second);
        }

        return getCell(p.first, p.second);
    }

    private fun movePlayer(player: Player, position: Point) {
        val path = findPath(player.sovereign.on.point, position);

        player.sovereign.travel(path);
    }

    private fun addWall(player: Player, a: Point, b: Point) {
        var cellA = getCell(a);
        var cellB = getCell(b);

        // Sort cellA and cellB to make sure cellA is always on the left or top of cellB
        if (cellA.x > cellB.x || cellA.y > cellB.y) {
            cellA = cellB.also { cellB = cellA }
        }

        if (cellA.distance(cellB) != 1) {
            throw Exception("Error: invalid wall");
        }

        var vertical = true

        if (cellA.x < cellB.x) {
            cellA.canRight = false;
            cellB.canLeft = false;
        } else if (cellA.y < cellB.y) {
            cellA.canDown = false;
            cellB.canUp = false;
            vertical = false
        }

        walls.add(Wall(cellA, cellB));
        view.addWall(cellB, vertical, player.colorToken);
    }

    private fun findPath(from: Point, to: Point): List<Cell> {
        val start = getCell(from)
        val end = getCell(to)

        if (start.distance(end) > Constant.MAX_DISTANCE) {
            return emptyList()
        }

        // Use BFS to search for the path, with depth limit of MAX_DISTANCE
        val queue = LinkedList<Pair<Cell, Int>>()
        val visited = mutableSetOf<Cell>()
        val parent = mutableMapOf<Cell, Cell>()

        visited.add(start)
        queue.add(Pair(start, 0))

        while (queue.isNotEmpty()) {
            val (current, depth) = queue.poll()

            if (current == end) {
                break
            }

            if (depth > Constant.MAX_DISTANCE) {
                continue
            }

            for (neighbor in getNeighbor(current)) {
                if (neighbor in visited || !current.canGo(neighbor)) {
                    continue
                }

                visited.add(neighbor)
                parent[neighbor] = current
                queue.add(Pair(neighbor, depth + 1))
            }
        }

        if (end !in visited) {
            return emptyList()
        }

        val path = mutableListOf<Cell>()
        var current: Cell? = end
        while (current != null) {
            path.add(current)
            current = parent[current]
        }
        path.reverse()
        return path
    }

    private fun getNeighbor(cell: Cell): List<Cell> {
        val neighbors = mutableListOf<Cell>();
        if (cell.x > 0) {
            neighbors.add(getCell(cell.x - 1, cell.y));
        }
        if (cell.x < Constant.WIDTH - 1) {
            neighbors.add(getCell(cell.x + 1, cell.y));
        }
        if (cell.y > 0) {
            neighbors.add(getCell(cell.x, cell.y - 1));
        }
        if (cell.y < Constant.HEIGHT - 1) {
            neighbors.add(getCell(cell.x, cell.y + 1));
        }
        return neighbors;
    }
}
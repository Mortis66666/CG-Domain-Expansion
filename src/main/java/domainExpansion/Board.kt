package domainExpansion

import com.codingame.game.Player
import view.BoardView
import java.util.*

class Board {
    private lateinit var view: BoardView;
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

    private fun getCell(x: Int, y: Int): Cell {
        return grid[y][x];
    }

    private fun getCell(p: Point): Cell {
        return getCell(p.first, p.second);
    }

    fun movePlayer(player: Player, position: Point) {
        findPath(player.sovereign.on.point, position);
        val path = listOf<Cell>(getCell(Point(0, 1)), getCell(Point(1, 1)))

        player.sovereign.travel(path);
    }

    fun addWall(a: Point, b: Point) {
        val cellA = getCell(a);
        val cellB = getCell(b);

        if (cellA.distance(cellB) != 1) {
            throw Exception("Error: invalid wall");
        }

        if (cellA.x < cellB.x) {
            cellA.canRight = false;
            cellB.canLeft = false;
        } else if (cellA.x > cellB.x) {
            cellA.canLeft = false;
            cellB.canRight = false;
        } else if (cellA.y < cellB.y) {
            cellA.canDown = false;
            cellB.canUp = false;
        } else if (cellA.y > cellB.y) {
            cellA.canUp = false;
            cellB.canDown = false;
        }

        view.addWall(cellA, cellB);
    }

    fun findPath(from: Point, to: Point): List<Cell> {
        val start = getCell(from);
        val end = getCell(to);

        if (start.distance(end) > Constant.MAX_DISTANCE) {
            return emptyList();
        }

        // Use BFS to search for the path, with depth limit of MAX_DISTANCE
        val queue = LinkedList<Cell>();
        val visited = mutableSetOf<Cell>();
        val parent = mutableMapOf<Cell, Cell>();

        queue.add(start);

        while (queue.isNotEmpty()) {
            val current = queue.poll();
            if (current == end) {
                break;
            }

            for (neighbor in getNeighbor(current)) {
                if (neighbor in visited || !current.canGo(neighbor)) {
                    continue;
                }

                visited.add(neighbor);
                parent[neighbor] = current;
                queue.add(neighbor);
            }
        }

        val path = mutableListOf<Cell>();
        var current: Cell? = end;
        while (current != null) {
            path.add(current);
            current = parent[current];
        }
        path.reverse();
        return path;
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
from queue import Queue
from functools import partial
import random
import sys

class Cell:
    def __init__(self):
        self.occupied = False
        self.walls = {"U": False, "R": False, "D": False, "L": False}  # Walls with borders set by default

    def occupy(self):
        self.occupied = True

    def disoccupy(self):
        self.occupied = False

    def get_empty_directions(self):
        return [direction for direction, wall in self.walls.items() if not wall]


class Board:
    def __init__(self, cols, rows):
        self.cols = cols
        self.rows = rows
        self.grid = [[Cell() for _ in range(cols)] for _ in range(rows)]  # Grid remains rows by cols

        # Set border walls
        for r in range(rows):
            for c in range(cols):
                if r == 0:
                    self.grid[r][c].walls["U"] = True
                if r == rows - 1:
                    self.grid[r][c].walls["D"] = True
                if c == 0:
                    self.grid[r][c].walls["L"] = True
                if c == cols - 1:
                    self.grid[r][c].walls["R"] = True

    def get_cell(self, x, y):
        if 0 <= y < self.rows and 0 <= x < self.cols:
            return self.grid[y][x]
        else:
            raise IndexError("Cell index out of bounds.")

    def cast_wall(self, x1, y1, x2, y2):
        if not (0 <= y1 < self.rows and 0 <= x1 < self.cols and
                0 <= y2 < self.rows and 0 <= x2 < self.cols):
            raise IndexError("Cell index out of bounds.")

        if abs(x1 - x2) + abs(y1 - y2) != 1:
            raise ValueError("Cells must be adjacent to cast a wall.")

        if y1 == y2:  # Horizontal neighbors
            if x1 < x2:
                self.grid[y1][x1].walls["R"] = True
                self.grid[y2][x2].walls["L"] = True
            else:
                self.grid[y1][x1].walls["L"] = True
                self.grid[y2][x2].walls["R"] = True
        else:  # Vertical neighbors
            if y1 < y2:
                self.grid[y1][x1].walls["D"] = True
                self.grid[y2][x2].walls["U"] = True
            else:
                self.grid[y1][x1].walls["U"] = True
                self.grid[y2][x2].walls["D"] = True

    def occupy_cell(self, x, y):
        self.get_cell(x, y).occupy()

    def disoccupy_cell(self, x, y):
        self.get_cell(x, y).disoccupy()

    def move(self, x1, y1, x2, y2):
        self.occupy_cell(x2, y2)
        self.disoccupy_cell(x1, y1)
        return x2, y2

    def floodfill(self, x, y, depth):
        visited = set()
        results = []

        # Directions mapping: up, right, down, left
        directions = [("U", 0, -1), ("R", 1, 0), ("D", 0, 1), ("L", -1, 0)]

        # Initialize BFS queue
        q = Queue()
        q.put((x, y, depth))  # Start with the initial position
        visited.add((x, y))  # Mark the starting cell as visited
        results.append((x, y))  # Add the starting position to results

        while not q.empty():
            cx, cy, d = q.get()
            print(f"Exploring ({cx}, {cy}) with depth {d}", file=sys.stderr, flush=True)

            # If the depth is 0, stop expanding further
            if d <= 0:
                continue

            # Explore neighbors
            for direction, dx, dy in directions:
                nx, ny = cx + dx, cy + dy
                print(f"Checking neighbor ({nx}, {ny})", file=sys.stderr, flush=True)

                if not (0 <= ny < self.rows and 0 <= nx < self.cols):  # Ensure within bounds
                    print("Out of bounds", file=sys.stderr, flush=True)
                    continue

                # Skip visited cells
                if (nx, ny) in visited:
                    print("Already visited", file=sys.stderr, flush=True)
                    continue

                # Skip if the direction is blocked by a wall or if the cell is occupied
                if self.grid[cy][cx].walls[direction] or self.get_cell(nx, ny).occupied:
                    print("Wall or occupied", file=sys.stderr, flush=True)
                    continue

                # Add the valid neighbor to visited and the queue
                visited.add((nx, ny))
                q.put((nx, ny, d - 1))  # Decrement depth for the next level
                results.append((nx, ny))  # Append the current position with updated depth

        return results


    def build(self, x, y, direction):
        """
        Adds a wall in the specified direction ('U', 'D', 'L', 'R') for cell (x, y).
        Updates the neighboring cell's corresponding wall.
        """
        if direction not in ["U", "D", "L", "R"]:
            raise ValueError("Invalid direction. Use 'U', 'D', 'L', 'R'.")

        cell = self.get_cell(x, y)

        if direction == "U" and y > 0:  # Add wall upward
            cell.walls["U"] = True
            self.grid[y - 1][x].walls["D"] = True
        elif direction == "D" and y < self.rows - 1:  # Add wall downward
            cell.walls["D"] = True
            self.grid[y + 1][x].walls["U"] = True
        elif direction == "L" and x > 0:  # Add wall to the left
            cell.walls["L"] = True
            self.grid[y][x - 1].walls["R"] = True
        elif direction == "R" and x < self.cols - 1:  # Add wall to the right
            cell.walls["R"] = True
            self.grid[y][x + 1].walls["L"] = True
        else:
            raise IndexError("Cannot build wall in the specified direction.", x, y, direction)


width, height = map(int, input().split())
x, y = map(int, input().split())
ox, oy = map(int, input().split())

fprint = partial(print, file=sys.stderr, flush=True)

MAX_DISTANCE = 3

board = Board(width, height)
board.occupy_cell(x, y)
board.occupy_cell(ox, oy)


while True:
    *o, direction = input().split()
    nox, noy = map(int, o)

    if direction != "_":
        # Move the opponent
        ox, oy = board.move(ox, oy, nox, noy)

        board.build(ox, oy, direction)
        
    # Move the player
    moves = board.floodfill(x, y, MAX_DISTANCE)
    move = random.choice(moves)

    x, y = board.move(x, y, *move)

    directions = board.get_cell(x, y).get_empty_directions()
    fprint(directions)
    direction = random.choice(directions)

    board.build(x, y, direction)

    print(x, y, direction)

    

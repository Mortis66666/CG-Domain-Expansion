import java.util.*;

public class Agent2 {
    private static final int GRID_SIZE = 7;
    private static final int MAX_DISTANCE = 3;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int width = in.nextInt(), height = in.nextInt();

        while (true) {
            int startX = in.nextInt();
            int startY = in.nextInt();
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();
            int numWalls = in.nextInt();

            // Track walls in a set of pairs for easy access
            Set<String> walls = new HashSet<>();
            for (int i = 0; i < numWalls; i++) {
                int ax = in.nextInt();
                int ay = in.nextInt();
                int bx = in.nextInt();
                int by = in.nextInt();
                walls.add(getWallKey(ax, ay, bx, by));
                walls.add(getWallKey(bx, by, ax, ay));  // Add both directions
            }

            // BFS initialization
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
            queue.add(new int[] {startX, startY, 0}); // {x, y, distance}
            visited[startX][startY] = true;

            int[] targetCell = {startX, startY, 0, Integer.MAX_VALUE}; // {x, y, distance, distToOpponent}

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                int dist = current[2];

                if (dist > MAX_DISTANCE) continue; // Skip cells beyond max distance

                // Calculate Manhattan distance to the opponent
                int distToOpponent = Math.abs(x - opponentX) + Math.abs(y - opponentY);

                // Check if this cell is a better candidate:
                // - It must be within MAX_DISTANCE.
                // - Prefer cells that are further from the start.
                // - Among cells with the same distance, prefer those closer to the opponent.
                if ((dist > targetCell[2]) ||
                        (dist == targetCell[2] && distToOpponent < targetCell[3])) {
                    targetCell = new int[] {x, y, dist, distToOpponent};
                }

                // Explore neighbors
                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE &&
                            !visited[nx][ny] && !walls.contains(getWallKey(x, y, nx, ny))) {
                        visited[nx][ny] = true;
                        queue.add(new int[] {nx, ny, dist + 1});
                    }
                }
            }

            // Output the target cell coordinates
            System.out.println(targetCell[0] + " " + targetCell[1] + " " + randomMove());
        }
    }

    // Helper method to create a unique key for walls
    private static String getWallKey(int ax, int ay, int bx, int by) {
        return ax + "," + ay + "-" + bx + "," + by;
    }

    private static String randomMove() {
        Random random = new Random();
        String[] moves = {"U", "D", "L", "R"};
        return moves[random.nextInt(moves.length)];
    }
}

import java.util.*;

public class Agent1 {
    private static final int GRID_SIZE = 7;
    private static final int MAX_DISTANCE = 3;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random();

        while (true) {
            int startX = in.nextInt();
            int startY = in.nextInt();
            int opponentX = in.nextInt(); // Unused, for simplicity
            int opponentY = in.nextInt(); // Unused, for simplicity
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

            // BFS to collect all reachable cells within MAX_DISTANCE
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
            List<int[]> accessibleCells = new ArrayList<>();

            queue.add(new int[] {startX, startY, 0}); // {x, y, distance}
            visited[startX][startY] = true;

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            while (!queue.isEmpty()) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                int dist = current[2];

                if (dist <= MAX_DISTANCE) {
                    accessibleCells.add(new int[] {x, y});  // Add accessible cell
                }
                if (dist >= MAX_DISTANCE) {
                    continue; // Skip cells beyond the max distance
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

            // Randomly select an accessible cell within range
            if (!accessibleCells.isEmpty()) {
                int[] randomCell = accessibleCells.get(random.nextInt(accessibleCells.size()));
                System.out.println(randomCell[0] + " " + randomCell[1] + " D");
            } else {
                // If no accessible cell found within range, output the starting cell
                System.out.println(startX + " " + startY + " " + randomMove());
            }
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

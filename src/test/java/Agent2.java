import java.util.Scanner;

public class Agent2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int x = 6, y = 6;

        while (true) {
            String input = scanner.nextLine();

            // Move 0~3 steps away from (x, y), and not exceeding 0 and 6
            int steps = (int) (Math.random() * 4);

            for (int i = 0; i < steps; i++) {
                // Randomly move (up, down, left, right)
                int move = (int) (Math.random() * 4);
                switch (move) {
                    case 0:
                        x = Math.max(0, x - 1);
                        break;
                    case 1:
                        x = Math.min(6, x + 1);
                        break;
                    case 2:
                        y = Math.max(0, y - 1);
                        break;
                    case 3:
                        y = Math.min(6, y + 1);
                        break;
                }
            }

            System.out.println(x + " " + y + " L");
        }
    }
}

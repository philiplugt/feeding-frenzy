/**
 * FrenzyLauncher.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

public class FrenzyLauncher {

    // Default board size
    private static int size = Constants.SIZE;

    public static void main(String[] args) {

        // Check for command and validity
        if (args.length > 1) {
            System.out.println("Usage: Only provide 0 or 1 command argument(s)");
            return;
        } else {
            if (args.length == 1) {
                size = Integer.parseInt(args[0]);
                if (size < 7 || size > 30) {
                    System.out.println("Usage: Board size N must be between 7 and 30");
                    return;
                }
            }
        }

        Model game = new Model(size);
        View view = new View(size);
        new UserController(view, game);

        game.start();
    }
}
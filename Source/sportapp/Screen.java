package sportapp;
import java.util.Scanner;

/**
 * Screen interface represents a UI screen that can be displayed to a user.
 */
public interface Screen {
    /**
     * Displays the screen, handles input, and returns the next route.
     * @param scanner input scanner
     * @param user current user
     * @return next application route
     */
    Route display(Scanner scanner, User user);
}
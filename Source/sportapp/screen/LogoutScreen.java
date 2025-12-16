package sportapp.screen;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;

/**
 * Represents the logout screen in the sport management system.
 * <p>
 * This screen handles the logout process and redirects the user to the portal.
 */
public class LogoutScreen implements Screen {

    /**
     * Constructs a LogoutScreen instance.
     * <p>
     * Initializes the LogoutScreen object with default values.
     */
    public LogoutScreen() {
        // Default constructor
    }

    public Route display(Scanner scanner, User user) {
        System.out.println("You have been logged out. Now jump to Portal.");
        return Route.PORTAL;
    }
}
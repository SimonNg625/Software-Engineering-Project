package sportapp.screen;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;

/**
 * Represents the portal screen in the application.
 * <p>
 * This screen allows users to navigate to login, register, reset password, or exit options.
 */
public class PortalScreen implements Screen {

    /**
     * Constructs a PortalScreen instance.
     * <p>
     * This constructor initializes the PortalScreen object with default values.
     */
    public PortalScreen() {
        // Default constructor
    }

    @Override
    public Route display(Scanner scanner, User user) {
        System.out.println("======= Portal =======");
        System.out.println("1: Login");
        System.out.println("2: Register");
        System.out.println("3: Reset Password");
        System.out.println("E: Exit");
        System.out.println("======================");
        System.out.print("Please input function number (1-3) or Exit (E): ");
        String choice = scanner.nextLine();
        
        // System.out.println("");
        switch (choice) {
            case "1":
                return Route.LOGIN;
            case "2":
                return Route.REGISTER;
            case "3":
                return Route.RESET;
            case "E":
                return Route.EXIT;
            default:
                System.out.println("Invalid choice. Please try again.");
                return Route.PORTAL; // Stay on the same screen
        }
    }
}
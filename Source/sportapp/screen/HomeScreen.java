package sportapp.screen;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;

/**
 * Represents the home screen in the sport management system.
 * <p>
 * This class implements the Screen interface and provides the main menu for users
 * to navigate to different functionalities of the application.
 */
public class HomeScreen implements Screen {

    /**
     * Constructs a HomeScreen instance.
     * <p>
     * This constructor initializes the HomeScreen object with default values.
     */
    public HomeScreen() {
        // Default constructor
    }

    /**
     * Displays the HomeScreen to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to based on user input.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        String userInput;
        do {
            System.out.println("======= Home Page =======");
            System.out.println("1. Sport Facility Booking");
            System.out.println("2. Sport Equipment Booking");
            System.out.println("3. View Pending Booking");
            System.out.println("4. View Booking Record");
            System.out.println("L. Logout");
            System.out.println("E. Exit");
            System.out.println("========================");
            System.out.print("Please input function number (1-5), Logout (L) or Exit (E): ");
            userInput = scanner.nextLine();

            switch (userInput.toUpperCase()) {
                case "1":
                    return Route.FACILITY_BOOKING;
                case "2":
                    return Route.BOOK_EQUIPMENT;
                case "3":
                    return Route.CURRENT_BOOKINGS;
                case "4":
                    System.out.println("Function 'View Booking Record' is implemented.");
                    return Route.CONFIRMED_BOOKINGS;
                case "L":
                    return Route.LOGOUT;
                case "E":
                    return Route.EXIT;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (true);
    }
}

package sportapp;
import java.util.Scanner;

import sportapp.screen.BookEquipmentScreen;
import sportapp.screen.HomeScreen;
import sportapp.screen.LoginScreen;
import sportapp.screen.LogoutScreen;
import sportapp.screen.PaymentScreen;
import sportapp.screen.PortalScreen;
import sportapp.screen.RegisterScreen;
import sportapp.screen.ResetScreen;
import sportapp.ui.FacilityBookingUI;
import sportapp.ui.ViewConfirmedBookingUI;
import sportapp.ui.ViewCurrentBookingUI;

/**
 * The main class for the Sport Centre Management System.
 * This class initializes the application and provides the entry point for execution.
 */
public class SportApp {
    /**
     * Scanner instance for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user. This is shared across the application.
     */
    private static User currentUser = null;

    /**
     * Constructs a SportApp instance with the provided Scanner.
     *
     * @param scanner The Scanner object for reading user input.
     */
    public SportApp(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Sets the current user of the application.
     *
     * @param user The user to set as the current user.
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Retrieves the current user of the application.
     *
     * @return The currently logged-in user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Runs the main application loop, displaying screens and handling user navigation.
     */
    public void run() {
        System.out.println("Welcome to Sport Centre Management System.");
        Screen portalScreen = new PortalScreen();
        Screen loginScreen = new LoginScreen();
        Screen registerScreen = new RegisterScreen();
        Screen resetScreen = new ResetScreen();
        Screen logoutScreen = new LogoutScreen();
        Screen homeScreen = new HomeScreen();
        Screen facilityScreen = new FacilityBookingUI(scanner, currentUser);
        Screen bookEquipmentScreen = new BookEquipmentScreen();
        Screen currentBookingScreen = new ViewCurrentBookingUI(scanner, currentUser);
        Screen confirmedBookingScreen = new ViewConfirmedBookingUI();
        Screen paymentScreen = new PaymentScreen();

        Route router = Route.PORTAL;

        while(router != Route.EXIT){
            switch (router) {
                case PORTAL:
                    router = portalScreen.display(scanner, currentUser);
                    break;
                case LOGIN:
                    router = loginScreen.display(scanner, currentUser);
                    break;
                case REGISTER:
                    router = registerScreen.display(scanner, currentUser);
                    break;
                case RESET:
                    router = resetScreen.display(scanner, currentUser);
                    break;
                case HOME:
                    router = homeScreen.display(scanner, currentUser);
                    break;
                case FACILITY_BOOKING:
                    router = facilityScreen.display(scanner, currentUser);
                    break;
                case BOOK_EQUIPMENT:
                    router = bookEquipmentScreen.display(scanner, currentUser);
                    break;
                case CURRENT_BOOKINGS:
                    router = currentBookingScreen.display(scanner, currentUser);
                    break;
                case CONFIRMED_BOOKINGS:
                    router = confirmedBookingScreen.display(scanner, currentUser);
                    break;
                case PAYMENT:
                    router = paymentScreen.display(scanner, currentUser);
                    break;
                case LOGOUT:
                    router = logoutScreen.display(scanner, currentUser);
                    currentUser = null; // Clear the current user on logout
                    break;
                case EXIT:
                    break;
                default:
                    System.out.println("Invalid route. Returning to Portal.");
                    router = Route.PORTAL;
                    break;
            }
            System.out.println("");
        }
        System.out.println("Thank you for using the booking system! Exiting...");
    }
    
    
    /**
     * Prompts the user to continue or not based on their input.
     *
     * @param choice The initial choice provided by the user.
     * @param scanner The Scanner object for reading user input.
     * @return True if the user chooses to continue, false otherwise.
     */
    public static boolean ContinueOrNot(String choice, Scanner scanner) {
        choice = choice.toUpperCase();
        while(!choice.equals("Y") && !choice.equals("N")){
            System.out.println("Invalid input. Please try again.");
            System.out.println("Continue? (Y/N)");
            choice = scanner.nextLine();
        }

        if (choice.equals("N")) { return false; }
        
        return true;
    }
}


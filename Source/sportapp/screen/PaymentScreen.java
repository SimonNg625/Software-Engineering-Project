package sportapp.screen;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;

/**
 * Represents the payment screen in the application.
 * <p>
 * This screen allows users to select a payment method and complete their payment.
 */
public class PaymentScreen implements Screen {

    /**
     * Constructs a PaymentScreen instance.
     * <p>
     * This constructor initializes the PaymentScreen object with default values.
     */
    public PaymentScreen() {
        // Default constructor
    }

    @Override
    public Route display(Scanner scanner, User user) {
        System.out.println("\n======= Payment Page =======");
        System.out.println("1. Octopus Card");
        System.out.println("2. Payme");
        System.out.print("Please select one method for your payment[1/2]: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Paying with Octopus Card...");
                System.out.println("Payment successful! Now back to Home Page....");
                return Route.HOME;
            case "2":
                System.out.println("Paying with Payme...");
                System.out.println("Payment successful! Now back to Home Page....");
                return Route.HOME;
            default:
                System.out.println("Invalid choice. Please try again.");
                return Route.PAYMENT;
        }
    }
}
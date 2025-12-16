package sportapp.screen;
import java.util.Scanner;

import sportapp.PasswordStrengthAnalysis;
import sportapp.Route;
import sportapp.Screen;
import sportapp.SportApp;
import sportapp.User;
import sportapp.UserCollection;

/**
 * The ResetScreen class handles the password reset functionality for users.
 * <p>
 * This screen allows users to reset their password by verifying their security question
 * and setting a new password that meets the application's strength requirements.
 */
public class ResetScreen implements Screen {
    private static UserCollection userCollection = UserCollection.getInstance();

    /**
     * Constructs a ResetScreen instance.
     * <p>
     * This constructor initializes the ResetScreen object.
     */
    public ResetScreen() {
        // Default constructor
    }

    public Route display(Scanner scanner, User user) {
        System.out.println("======= Reset Page =======");
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        User myUser = userCollection.findUserByName(username);
        
        if (myUser == null) {
            System.out.println("The username does not exist. Please try again.");
            return Route.RESET;
        }
        
        System.out.println(myUser.getQuestion());
        System.out.println("Please answer this question:");
        String answer = scanner.nextLine();
        
        while (myUser.verifySercurityAnswer(answer) == false) {
            System.out.println("Your answer is wrong. Please try again.");
            System.out.println("Continue? (Y/N)");
            String choice = scanner.nextLine();
        
            if(SportApp.ContinueOrNot(choice, scanner) == false){
                return Route.PORTAL;
            }
        
            System.out.println(myUser.getQuestion());
            System.out.println("Please answer this question:");
            answer = scanner.nextLine();
        }
        
        System.out.println("Please enter your new password: (A strong password should be at least 8 characters long and include uppercase letters, lowercase letters, digits, and special characters.)");
        String newPassword = scanner.nextLine();
        
        while (PasswordStrengthAnalysis.isStrongPassword(newPassword) == false) {
            System.out.println("The password is not strong enough. Please enter another password.");
            System.out.println("Continue? (Y/N)");
            String choice = scanner.nextLine();
        
            if (SportApp.ContinueOrNot(choice, scanner) == false) {
                return Route.PORTAL;
            }
        
            System.out.println("Please enter your new password: (A strong password should be at least 8 characters long and include uppercase letters, lowercase letters, digits, and special characters.)");
            newPassword = scanner.nextLine();
        }
        
        myUser.setPassword(newPassword);
        System.out.println("Password reset successfully! Now jump to Home Page.");
        return Route.HOME;
    }
}
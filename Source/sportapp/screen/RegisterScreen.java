package sportapp.screen;
import java.util.Scanner;

import sportapp.PasswordStrengthAnalysis;
import sportapp.Route;
import sportapp.Screen;
import sportapp.SportApp;
import sportapp.User;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;

/**
 * Represents the register screen in the application.
 * <p>
 * This screen allows users to create a new account by providing a username, password, and security question.
 */
public class RegisterScreen implements Screen {
    private static UserCollection userCollection = UserCollection.getInstance();

    /**
     * Constructs a RegisterScreen instance.
     * <p>
     * This constructor initializes the RegisterScreen object with default values.
     */
    public RegisterScreen() {
        // Default constructor
    }

    public Route display(Scanner scanner, User user) {
        String username = "";
        String password = "";
        String question = "";
        String answer = "";
        
        System.out.println("======= Register Page =======");
        System.out.print("Please enter the username: ");
        username = scanner.nextLine();
        
        while(userCollection.findUserByName(username) != null){
            System.out.println("The username has already been taken. Please enter another username."); 
            System.out.println("Continue? (Y/N)");
            String choice = scanner.nextLine();
            if(SportApp.ContinueOrNot(choice, scanner) == false){
                return Route.PORTAL;
            }
            System.out.print("Please enter the username: ");
            username = scanner.nextLine();    
        }
        
        System.out.println("Please enter the password: (A strong password should be at least 8 characters long and include uppercase letters, lowercase letters, digits, and special characters.)");
        password = scanner.nextLine();
        
        while(PasswordStrengthAnalysis.isStrongPassword(password) == false){
            System.out.println("The password is not strong enough. Please enter another password.");
            System.out.println("Continue? (Y/N)");
            String choice = scanner.nextLine();
            if (SportApp.ContinueOrNot(choice, scanner) == false) {
                return Route.PORTAL;
            }

            System.out.println("Please enter the password: (A strong password should be at least 8 characters long and include uppercase letters, lowercase letters, digits, and special characters.)");
            password = scanner.nextLine();
        }
        
        System.out.print("Please set a security question: ");
        question = scanner.nextLine();
        System.out.print("Please set the answer for the security question: ");
        answer = scanner.nextLine();
        
        UserSecurityAnswer securityAnswer = new UserSecurityAnswer(question, answer);
        User newUser = userCollection.addUser(username, password, securityAnswer);
        SportApp.setCurrentUser(newUser);

        System.out.println("\nRegistered successfully!");
        System.out.printf("Username: %s\n", username);
        System.out.printf("Password: %s\n", password);
        System.out.printf("Security Question: %s\n", securityAnswer.getQuestion());
        System.out.printf("Security Question Answer: %s\n", securityAnswer.getAnswer());
        System.out.println("Now jump to Home Page.");
        
        return Route.HOME;
    }
}
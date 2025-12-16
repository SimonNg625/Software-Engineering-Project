package sportapp.screen;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.SportApp;
import sportapp.User;
import sportapp.UserCollection;

/**
 * Represents the login screen in the sport management system.
 * <p>
 * This screen allows users to log in by providing their username and password.
 */

public class LoginScreen implements Screen {
    private static UserCollection userCollection;
    private User loginUser; 
    
    /**
     * Constructs a LoginScreen instance.
     * <p>
     * Initializes the user collection and sets the login user to null.
     */
    public LoginScreen() {
        userCollection = UserCollection.getInstance();
        loginUser = null;
    }

    @Override
    public Route display(Scanner scanner, User user) {
        String username = "";
        String password = "";
        
        System.out.println("======= Login Page =======");
        System.out.print("Please enter your username: ");
        username = scanner.nextLine();
        loginUser = userCollection.findUserByName(username);

        System.out.print("Please enter your password: ");
        password = scanner.nextLine();
        
        if ((loginUser == null) || (loginUser.verifyPassword(password) == false)) {
            System.out.println("Your username or password is incorrect.");
            System.out.println("Continue? (Y/N)");
            String option = scanner.nextLine();
            
            if (SportApp.ContinueOrNot(option, scanner)) { return Route.LOGIN; }
            else { return Route.PORTAL; }
        }

        SportApp.setCurrentUser(loginUser);
        System.out.println("Login successfully! Now jump to Home Page.");
        return Route.HOME;
    }
}